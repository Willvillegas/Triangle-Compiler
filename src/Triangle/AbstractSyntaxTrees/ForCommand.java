/*
 * @(#)ForCommand.java                        2.1 2024/16/11
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
 * Grammar:
 *          For    ::=  for V-name := Expression to Expression  
                [by Integer-Literal]  
              do  single-Command 
 */

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class ForCommand extends Command {

  public ForCommand (Vname vnAST,Expression e1AST,Expression e2AST,IntegerLiteral ilAST, Command cAST, SourcePosition thePosition) {
    super (thePosition);
    Vn = vnAST;
    E1 = e1AST;
    E2 = e2AST;
    IL = (ilAST != null) ? ilAST : null; //el opcional
    C = cAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitForCommand(this, o);
  }
  //for
  public Vname Vn;
  // := (becomes)
  public Expression E1;
  // to
  public Expression E2;
  //[ by
  public IntegerLiteral IL;
  // ] do
  public Command C;
}
