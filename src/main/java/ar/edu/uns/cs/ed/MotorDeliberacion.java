package ar.edu.uns.cs.ed;

import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdalista.TdaLista;
import ar.edu.uns.cs.ed.tdas.tdapila.Stack;
import ar.edu.uns.cs.ed.tdas.tdamapeo.Map;
import ar.edu.uns.cs.ed.tdas.tdamapeo.MapeoHashAbierto;
import ar.edu.uns.cs.ed.tdas.tdaarbol.Arbol;
import ar.edu.uns.cs.ed.tdas.tdaarbol.Tree;
import ar.edu.uns.cs.ed.tdas.tdadiccionario.Diccionario;
import ar.edu.uns.cs.ed.tdas.tdapila.TDAPila;

import java.util.Iterator;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyStackException;

public class MotorDeliberacion {

    //Atributos
    private Stack<Operaciones> pilaOperaciones;
    private Tree<Opinion> arbolOpiniones;
    private Map<String, Position<Opinion>> posicionesPorId;
    private Map<String, PositionList<Opinion>> opinionesPorUsuario;
    private Map<String, Position<Opinion>> posicionEnListaUsuario;

    //Constructor
    public MotorDeliberacion() {
        pilaOperaciones = new TDAPila<Operaciones>();
        arbolOpiniones = new Arbol<Opinion>();
        posicionesPorId = new MapeoHashAbierto<String, Position<Opinion>>();
        opinionesPorUsuario = new MapeoHashAbierto<String, PositionList<Opinion>>();
        posicionEnListaUsuario = new MapeoHashAbierto<String, Position<Opinion>>();
    }

    /**
     * Registra una operación en la estructura que almacena las operaciones.
     * Las siguientes operaciones se pueden deshacer: "CREAR_DEBATE", "POSTEAR". El resto de las operaciones no se pueden deshacer.
     * 
     * La operación eliminar posteo no se puede deshacer, y si se intenta deshacer una operación cuando no hay operaciones registradas, se lanza una excepción.
     * 
     * 
     * @param operacion : La operación a registrar. 1: CREAR_DEBATE, 2: POSTEAR
     * @param detalle : Detalle de la operación, como el id del argumento o el id del usuario. El texto de la operación se almacena en mayúsculas para mantener consistencia. Por ejemplo: "CREAR_DEBATE:" + idRaiz.trim()
     * @param idArgumento : Identificador del argumento o de root en caso de ser el tema raíz.
     * @throws IllegalArgumentException si la operación es nula o vacía.
     */

    public void registrarOperacion(int operacion, String detalle, String idArgumento) {
        if (operacion != 1 && operacion != 2 && operacion != 3)
            throw new IllegalArgumentException("Operación invalida");

        if (detalle == null || detalle.trim().isEmpty())
            throw new IllegalArgumentException("Detalle invalido");

        if (idArgumento == null || idArgumento.trim().isEmpty())
            throw new IllegalArgumentException("ID invalido");

        Operaciones nueva = new Operaciones(operacion, detalle.trim().toUpperCase(), idArgumento.trim());
        pilaOperaciones.push(nueva);
    }

/**
 * Deshace la última operación registrada en el motor de deliveración.
 * La operación eliminar posteo no se puede deshacer, y si se intenta deshacer una operación cuando no hay operaciones registradas, se lanza una excepción.
 * @return
 * @throws EmptyStackException
 */
    public String deshacerUltimaOperacion() throws EmptyStackException {
        
        if(pilaOperaciones.isEmpty()){
            throw new EmptyStackException("No hay operaciones registradas");
        }

        Operaciones ultima = pilaOperaciones.top();
        String respuesta = "";

        if(ultima.getOperacion() == 3){
            throw new IllegalStateException("No se pudo deshacer la operación POSTEAR: " + "la ultima operacion fue ELIMINAR_POSTEO");
        }
        else if (ultima.getOperacion() == 1) {
                
                //deshacer CREAR_DEBATE
                arbolOpiniones = new Arbol<Opinion>();
                posicionesPorId = new MapeoHashAbierto<String, Position<Opinion>>();
                opinionesPorUsuario = new MapeoHashAbierto<String, PositionList<Opinion>>();
                posicionEnListaUsuario = new MapeoHashAbierto<String, Position<Opinion>>();
                
                pilaOperaciones.pop();
                respuesta = "Se deshizo la operación " + ultima.getDetalle();
        } 
        else if(ultima.getOperacion() == 2){
            
            //deshacer POSTEAR
            String id= ultima.getId();
            Position<Opinion> pos = posicionesPorId.get(id);

            if(pos == null){
                throw new IllegalStateException("no se puede deshacer la operacion postear");
            }

            Opinion opinion = pos.element();
            String idUsuario = opinion.getUsuario().getId();

            //eliminar el postedo y todo el subarbol
            eliminarPosteoSinRegistrar(idUsuario, id);

            //sacar de la pila operacion POSTEAR
                pilaOperaciones.pop();
                respuesta = "Se deshizo la operacion POSTEAR: " + id;
        
        } else {
            throw new IllegalStateException("Operacion desconocida");
        }

        return respuesta;
    }


/**
 * Crea un nuevo debate con un argumento raíz.
 * @param idRaiz   : Identificador único del tema raíz.
 * @param texto:    Texto del tema raíz.
 * @param idUsuario: Identificador del usuario.
 * @param nombreUsuario: Nombre del usuario.
 * 
 * El método valida que los parámetros no sean nulos o vacíos, y que el identificador de la raíz sea numérico.
 * Si el árbol ya tiene un debate, lanza una excepción.
 * El método registra la operación en la pila de operaciones para permitir deshacerla posteriormente.
 * @throws IllegalArgumentException si algún parámetro es inválido.
 * @throws IllegalStateException si ya existe un debate en curso.
 * Crea un usuario y un tema raíz, y los agrega al árbol y a las estructuras correspondientes.
 */
    public void crearDebate(String idRaiz, String texto, String idUsuario, String nombreUsuario) {
    
        //Si algun parametro es inválido
        if (idRaiz == null || idRaiz.trim().isEmpty() ||
            texto == null || texto.trim().isEmpty() ||
            idUsuario == null || idUsuario.trim().isEmpty() ||
            nombreUsuario == null || nombreUsuario.trim().isEmpty())
            throw new IllegalArgumentException("Datos invalidos");

        //Si ya existe un debate en curso
        if (!arbolOpiniones.isEmpty())
            throw new IllegalStateException("Ya existe un debate");

        pilaOperaciones = new TDAPila<Operaciones>();

        String id = idRaiz.trim();
        Usuario usuario = new Usuario(idUsuario.trim(), nombreUsuario.trim());
        Opinion raiz = new Opinion(id, texto.trim(), "ROOT", usuario);
        
        arbolOpiniones.createRoot(raiz);
        Position<Opinion> posicionRaiz = arbolOpiniones.root();
        posicionesPorId.put(id, posicionRaiz);

        PositionList<Opinion> lista = new TdaLista<Opinion>();
        lista.addLast(raiz);
        posicionEnListaUsuario.put(id,lista.last());
        opinionesPorUsuario.put(usuario.getId(), lista);

        registrarOperacion(1, "CREAR_DEBATE:" + id, id);

    }

    public void Postear(Usuario usr, String idNuevo, String idPadre, String postura, String texto) {

        //validacion de arbol vacío
        if (arbolOpiniones.isEmpty()) 
            throw new IllegalStateException("No hay ningún debate creado");

        //validacion de parametros nulos y vacios
        if ((usr == null) || (usr.getId() == null) || (usr.getId().trim().isEmpty()) 
            || (idNuevo == null) || (idNuevo.trim().isEmpty()) 
            || (idPadre == null) || (idPadre.trim().isEmpty())
            || (postura == null) || (postura.trim().isEmpty())
            || (texto == null) || (texto.trim().isEmpty())) 
            throw new IllegalArgumentException("alguno/s de los parametros son invalidos");

        //validacion de postura y estandarizacion (mayusculas y sin espacios)
        String posturaMayus = postura.trim().toUpperCase();
        if (!posturaMayus.equals("AFAVOR") && (!posturaMayus.equals("ENCONTRA")))
            throw new IllegalArgumentException("La postura es incorrecta");

        //conseguimos y validamos al padre y al posteo
        Position<Opinion> padre = posicionesPorId.get(idPadre.trim());     //el mapa es lo que permite que el orden sea O(1)
        if (padre == null) throw new IllegalArgumentException("El padre no existe");

        String idPosteo = idNuevo.trim();
        if (posicionesPorId.get(idPosteo) != null) throw new IllegalArgumentException("El ID ya existe");

        //creamos opinion, la agregamos al arbol y metemos su posicion al mapa posicionesPorId
        Opinion opinion = new Opinion(idPosteo,texto,posturaMayus,usr);
        Position<Opinion> pos = arbolOpiniones.addLastChild(padre,opinion);
        posicionesPorId.put(idPosteo,pos);

        //conseguimos la lista de opiniones del usuario y le agregamos la nueva opinion
        String idUsuario = usr.getId().trim();
        PositionList<Opinion> lista = opinionesPorUsuario.get(idUsuario);
        if (lista == null) {             //primera opinion del usuario
            lista = new TdaLista<Opinion>();
            opinionesPorUsuario.put(idUsuario,lista);
        }
        lista.addLast(opinion);
        posicionEnListaUsuario.put(idPosteo, lista.last());

        //registramos la operacion por si hay que deshacerla
        registrarOperacion(2,"POSTEAR:" + idPosteo, idPosteo);
    }

    // Elimina un posteo y todo el subárbol.
    public void eliminarPosteo(String idUsuario, String idPosteo) {

        eliminarPosteoSinRegistrar(idUsuario, idPosteo);
        registrarOperacion(3, "ELIMINAR_POSTEO:" + idPosteo.trim(), idPosteo.trim());
    
    }

    private void eliminarPosteoSinRegistrar(String idUsuario, String idPosteo){

        //validacion de parametros nulos y vacios
        if ((idUsuario == null) || (idPosteo == null) 
            || (idUsuario.trim().isEmpty() || (idPosteo.trim().isEmpty())))
                throw new IllegalArgumentException("IdUsuario y/o IdPosteo incorrectos");

        if(arbolOpiniones.isEmpty())
            throw new IllegalStateException("No hay debate activo");
        
        Position<Opinion> pos = posicionesPorId.get(idPosteo.trim());

        //validaciones del posteo
        if (pos == null) 
            throw new IllegalArgumentException("El posteo no existe");

        if (!pos.element().getUsuario().getId().trim().equals(idUsuario.trim())) 
            throw new IllegalStateException("Este posteo no es del usuario ingresado");

        //el posteo es la raiz
        if (arbolOpiniones.isRoot(pos)) {
            arbolOpiniones = new Arbol<Opinion>();
            posicionesPorId = new MapeoHashAbierto<String, Position<Opinion>>();
            opinionesPorUsuario = new MapeoHashAbierto<String, PositionList<Opinion>>();
            posicionEnListaUsuario = new MapeoHashAbierto<String, Position<Opinion>>();
        }
        else{
            //obtenemos subarbol
            PositionList<String> listaIds = new TdaLista<String>();
            PositionList<Position<Opinion>> listaPosiciones = new TdaLista<Position<Opinion>>();
            obtenerIdsYPosicionesSubarbol(pos, listaIds, listaPosiciones);

            //eliminamos los posteos de todo el subarbol
            Iterator<String> itIds = listaIds.iterator();
            Iterator<Position<Opinion>> itPos = listaPosiciones.iterator();
            
            while (itPos.hasNext()) {
                String idNodo = itIds.next();
                Position<Opinion> p = itPos.next();
                String autorPosteo = p.element().getUsuario().getId().trim();

                //buscar la lista de opiniones de ese usuario
                PositionList<Opinion> lista = opinionesPorUsuario.get(autorPosteo);

                //buscar la pos dentro de la lista
                Position<Opinion> posicionLista = posicionEnListaUsuario.get(idNodo);

                //eliminar nodo del arbol
                arbolOpiniones.removeExternalNode(p);

                //eliminar de la lista del usuario
                lista.remove(posicionLista);

                //actualizar mapeos
                posicionEnListaUsuario.remove(idNodo);    
                posicionesPorId.remove(idNodo);

                // Si el usuario no tiene opiniones, quitamos su entrada
                if (lista.isEmpty()) {
                    opinionesPorUsuario.remove(autorPosteo);
                }
            }
        }
    }

    //obtiene las posiciones en postorden.
    private void obtenerIdsYPosicionesSubarbol(Position<Opinion> nodo, PositionList<String> ids, PositionList<Position<Opinion>> posiciones) {
    
        for(Position<Opinion> hijo : arbolOpiniones.children(nodo)){
            obtenerIdsYPosicionesSubarbol (hijo, ids, posiciones);
        }
        ids.addLast(nodo.element().getIdArgumento());
        posiciones.addLast(nodo);
    
    }

    //limpia todo el debate, dejando la estructura vacía.   
    public void clearDebate() {
        arbolOpiniones = new Arbol<Opinion>();
        posicionesPorId = new MapeoHashAbierto<String, Position<Opinion>>();
        opinionesPorUsuario = new MapeoHashAbierto<String, PositionList<Opinion>>();
        pilaOperaciones = new TDAPila<Operaciones>();
        posicionEnListaUsuario = new MapeoHashAbierto<String, Position<Opinion>>();
    }

    //consultar por usuario, devuelve una lista de argumentos que pertenecen a ese usuario.
    public PositionList<Opinion> consultarArgumentosPorUsuario(String idUsuario) {

        //Si el ID del usuario es inválido
        if (idUsuario == null || idUsuario.trim().isEmpty())
            throw new IllegalArgumentException("ID de usuario invalido");

        PositionList<Opinion> listaArgumentos = opinionesPorUsuario.get(idUsuario.trim());

        if (listaArgumentos == null)
            listaArgumentos = new TdaLista<Opinion>();

        return listaArgumentos;
}

    /**
     * Calcula la polarización de todos los nodos del debate.
     * La polarización se define como la cantidad de argumentos a favor y en contra que existen en el subárbol que tiene como raíz cada nodo.
     * @throws IllegalStateException si no hay un debate activo.
     * @return Un diccionario cuya clave es el ID del nodo y el valor es un diccionario con la polarización: 
     * "AFAVOR" y "ENCONTRA" y sus respectivos conteos.
     */
    public Diccionario<String,Diccionario<String,Integer>> calcularPolarizacionDebate(){
         if (arbolOpiniones == null || arbolOpiniones.isEmpty()){
            throw new IllegalStateException("no hay un debate activo");
        }
            Diccionario<String, Diccionario<String,Integer>> resultado= new Diccionario<String, Diccionario<String,Integer>>();
            Position<Opinion> raiz = arbolOpiniones.root();

            recorrerPolarizaciones(raiz,resultado);
            return resultado;
    }

    private int[] recorrerPolarizaciones(Position<Opinion> r, Diccionario<String, Diccionario<String,Integer>> d){
        String id= r.element().getIdArgumento();
        int[] conteos = new int[2];

        String postura = r.element().getPostura();
        if (postura.equals("AFAVOR")) {
            conteos[0]++;
        }
        else if (postura.equals("ENCONTRA")) {
                conteos[1]++;
            }
        
        for (Position<Opinion> p : arbolOpiniones.children(r) ){
            int[] conteosHijos = recorrerPolarizaciones(p,d);
            conteos[0] = conteos[0] + conteosHijos[0];
            conteos[1] = conteos[1] + conteosHijos[1];
        }

        Diccionario<String,Integer> polarizacion = new Diccionario<String,Integer>();
        polarizacion.insert("AFAVOR", conteos[0]);
        polarizacion.insert("ENCONTRA", conteos[1]);
        d.insert(id, polarizacion);

        return conteos;
    }

     /**
    * Calcula la polarización de un nodo específico en el árbol de debate.
    * La polarización se define como la cantidad de argumentos a favor y en contra que existen en el subárbol que tiene como raíz el nodo especificado por idNodo.
    * @throws IllegalStateException si no hay un debate activo. 
    * @param idNodo : Identificador del nodo para el cual se desea calcular la polarización.
    * @return Un DiccionarioHashAbierto<String,Integer> que contiene la polarización del nodo, con las claves "AFAVOR" y "ENCONTRA" y sus respectivos conteos.
    */
    public Diccionario<String,Integer> calcularPolarizacionNodo(String idNodo){
        if (arbolOpiniones == null || arbolOpiniones.isEmpty()){
            throw new IllegalStateException("no hay debate activo");
        }
        Position<Opinion> nodo= posicionesPorId.get(idNodo);
        if (nodo ==null){
            throw new IllegalArgumentException("no existe ese nodo");
        }
        int[] conteos = new int[2];
        contarPolarizaciones(nodo,conteos);
        Diccionario<String,Integer> resultado= new Diccionario<String,Integer>();
        resultado.insert("AFAVOR", conteos[0]);
        resultado.insert("ENCONTRA", conteos[1]);
        return resultado;
       
    }

    private void contarPolarizaciones(Position<Opinion> n, int []c){
        Opinion opinion= n.element();
        if (opinion.getPostura().equals("AFAVOR")){
            c[0]++;
        }else {
            if(opinion.getPostura().equals("ENCONTRA")){
                c[1]++;
            }
        }
        for (Position<Opinion> h : arbolOpiniones.children(n)){
            contarPolarizaciones(h, c);
        }
    }
    
 /*** Mostrar árbol por consola ***/
 
    /**
     * Imprime por consola, en modo texto, el contenido del árbol de debate,
     * con indentación jerárquica que muestra la estructura padre-hijo.
     * Cada nodo se muestra con su id, postura, usuario y texto del argumento.
     */
    public void imprimirDebate() {
        if (arbolOpiniones == null || arbolOpiniones.isEmpty()) {
            System.out.println("(El árbol de debate está vacío.)");
            return;
        }

        try {
            Position<Opinion> raiz = arbolOpiniones.root();
            System.out.println(formatearNodo(raiz.element()));
            imprimirHijos(raiz, "");
        } catch (Exception e) {
            System.out.println("No se pudo imprimir el árbol: " + e.getMessage());
        }
    }

    /**
     * Imprime recursivamente los hijos de 'nodo', usando 'prefijo' para
     * mantener la indentación acumulada de los niveles superiores y
     * conectores tipo "├──" / "└──" para el nivel actual.
     */
    private void imprimirHijos(Position<Opinion> nodo, String prefijo) throws InvalidPositionException {
        PositionList<Position<Opinion>> hijos = new TdaLista<Position<Opinion>>();
        for (Position<Opinion> hijo : arbolOpiniones.children(nodo)) {
            hijos.addLast(hijo);
        }

        for (Position<Opinion> hijo : arbolOpiniones.children(nodo)) {
            boolean esUltimo = (hijo == hijos.last().element());
            
            String conector = esUltimo ? "└── " : "├── ";
            System.out.println(prefijo + conector + formatearNodo(hijo.element()));

            String prefijoHijo = prefijo + (esUltimo ? "    " : "│   ");
            imprimirHijos(hijo, prefijoHijo); 
        }
    }

    /** Arma la línea de texto que representa a un Argumento en la impresión. */
    private String formatearNodo(Opinion a) {
        if (a == null) {
            return "(argumento nulo)";
        }
        return "[" + a.getIdArgumento() + "] (" + a.getPostura() + ") "
                + "usuario=" + a.getUsuario().getId() + " -> " + a.getTexto();
    }

    public void historialOperaciones() {
        if (pilaOperaciones.isEmpty()) {
            System.out.println("No hay operaciones registradas");
            return;
        }

        // creamos pila auxiliar para invertir orden
        Stack<Operaciones> pilaAux = new TDAPila<Operaciones>();
        while (!pilaOperaciones.isEmpty())
            pilaAux.push(pilaOperaciones.pop());

        //mostramos de la primera a la ultima operacion
        int numero = 1;
        while (!pilaAux.isEmpty()) {
            Operaciones tope = pilaAux.top();
            System.out.println("Operacion N° "+ numero +": "+ tope.getDetalle());
            pilaOperaciones.push(pilaAux.pop());
            numero++;
        }
    }

}