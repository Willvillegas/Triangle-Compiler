/*
 * @(#)ProcDeclaration.java                        2.1 2003/10/07
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
 * Extended Triangle Compiler
 * @author GF-Wilfredo Villegas
 */

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class ProcTypeDenoter extends TypeDenoter {

  public ProcTypeDenoter (FormalParameterSequence fpsAST,
  		   Command cAST, SourcePosition thePosition) {
    super (thePosition);
    FPS = fpsAST;
    C = cAST;
    this.T  = new AnyTypeDenoter(new SourcePosition());
  }


  public Object visit (Visitor v, Object o) {
    return v.visitProcTypeDenoter(this, o);
  }

  public TypeDenoter T;
  public FormalParameterSequence FPS;
  public Command C;

    @Override
    public boolean equals(Object obj) {
        return this.equals(obj);
    }
}
