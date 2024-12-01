/*
 * @(#)FuncDeclaration.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */
/**
 * Extended triangle compiler
 * @author GF-Wilfredo Villegas 
 */

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class FuncTypeDenoter extends TypeDenoter {

  public FuncTypeDenoter (FormalParameterSequence fpsAST,
  		   TypeDenoter tAST, Expression eAST,
                   SourcePosition thePosition) {
    super (thePosition);
    FPS = fpsAST;
    T = tAST;
    E = eAST;
  }
  

  public Object visit (Visitor v, Object o) {
    return v.visitFuncTypeDenoter(this, o);
  }

  
  public FormalParameterSequence FPS;
  public TypeDenoter T;
  public Expression E;

    @Override
    public boolean equals(Object obj) {
        return this.equals(obj);
    }
  
}
