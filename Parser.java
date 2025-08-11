/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/

import java.util.LinkedList;
import java.util.Stack;
import java.lang.Math;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    // Stacks para evaluar en el momento
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        // Recursive Descent Parser
        // Imprime si el input fue aceptado
        boolean acc = S();
        System.out.println("Aceptada? " + acc);

        // Shunting Yard Algorithm
        // Imprime el resultado de operar el input
        if(acc) System.out.println("Resultado: " + this.operandos.peek());

        // Verifica si terminamos de consumir el input
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            
            // Codigo para el Shunting Yard Algorithm
            
            if (id == Token.NUMBER) {
				// Encontramos un numero
				// Debemos guardarlo en el stack de operandos
				operandos.push( this.tokens.get(this.next).getVal() );

			} else if (id == Token.SEMI) {
				// Encontramos un punto y coma
				// Debemos operar todo lo que quedo pendiente
				while (!this.operadores.empty()) {
					popOp();
				}
				
			} else {
				// Encontramos algun otro token, es decir un operador
				// Lo guardamos en el stack de operadores
				// Que pushOp haga el trabajo, no quiero hacerlo yo aqui
				pushOp( this.tokens.get(this.next) );
			}
			
            
            this.next++;
            return true;
        }
        return false;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        

        /* El codigo de esta seccion se explicara en clase */

        switch(op.getId()) {
        	case Token.PLUS:
        		return 1;

            case Token.MINUS:
                return 1;
        	case Token.MULT:
        		return 2;

            case Token.DIV:
                return 2;

            case Token.MOD:
        		return 2;

            case Token.EXP:
        		return 3;
        	default:
        		return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();


        /* El codigo de esta seccion se explicara en clase */

        if (op.equals(Token.PLUS)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        
        	this.operandos.push(a + b);
        } else if (op.equals(Token.MINUS)) {
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	
        	
        	this.operandos.push(b - a);
        } else if (op.equals(Token.MULT)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	
        	this.operandos.push(a * b);
        } else if(op.equals(Token.DIV)){
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	
        	
        	this.operandos.push(b / a);
        } else if(op.equals(Token.MOD)){
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	
        	
        	this.operandos.push(b % a);
        } else if(op.equals(Token.EXP)){
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	
        	
        	this.operandos.push(Math.pow(b, a));
        }
    }

    private void pushOp(Token op) {
    

        /* Casi todo el codigo para esta seccion se vera en clase */
    	
    	// Si no hay operandos automaticamente ingresamos op al stack

        if(operandos.isEmpty()){operadores.add(op);}
        else if(!operadores.isEmpty() && pre(op)<=pre( operadores.peek()) ){
                
            while(!operadores.isEmpty()) popOp();
            operadores.add(op);
            
        }else{
            operadores.add(op);
        }
    	// Si si hay operandos:
    		// Obtenemos la precedencia de op
        	// Obtenemos la precedencia de quien ya estaba en el stack
        	// Comparamos las precedencias y decidimos si hay que operar
        	// Es posible que necesitemos un ciclo aqui, una vez tengamos varios niveles de precedencia
        	// Al terminar operaciones pendientes, guardamos op en stack

    }

    private boolean S() {
        return E() && term(Token.SEMI);
    }

    private boolean E() {
        int save = next;
        
        next = save;
        if(E1()){return true;}

        next = save;
        if(E2()){return true;}

        next = save;
        if(E3()){return true;}

        return false;
    }

    private boolean N() {
        int save = next;
        
        next = save;
        if(N1()){return true;}

        next = save;
        if(N2()){return true;}

        next = save;
        if(N3()){return true;}

        next = save;
        if(N4()){return true;}

        next = save;
        if(N5()){return true;}

        next = save;
        if(N6()){return true;}

        next = save;
        if(N7()){return true;}

        return false;
    }

    private boolean E1(){ return term(Token.UNARY) && E() && N();}

    private boolean E2(){ return term(Token.LPAREN) && E() && term(Token.RPAREN) && N();}

    private boolean E3(){return term(Token.NUMBER) && N();}

    private boolean N1(){ return term(Token.PLUS) && E() && N();}

    private boolean N2(){ return term(Token.MINUS) && E() && N();}

    private boolean N3(){ return term(Token.MULT) && E() && N();}

    private boolean N4(){ return term(Token.DIV) && E() && N();}

    private boolean N5(){ return term(Token.MOD) && E() && N();}

    private boolean N6(){ return term(Token.EXP) && E() && N();}

    private boolean N7(){ return true;}
    
}
