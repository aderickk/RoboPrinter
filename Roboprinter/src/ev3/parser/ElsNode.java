/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ev3.parser;

import java.util.ArrayList;

/**
 *
 * @author David Garcia
 */
public class ElsNode {
    private String function;
    private String[] args;

    ElsNode(String function, String args) {
        this.function = function;
        this.args = args.split(",");
    }

    public String getFunction() {
        return function;
    }

    public String[] getArgs() { 
        for (int i = 0; i < args.length; i++) {
            args[i]=args[i].replaceAll("\"","").trim();
        }
        return args;
    }

    @Override
    public String toString() {
        return function + "(" + args.toString() + ");\n";
    }
}
