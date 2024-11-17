/*
 * @(#)DoWhileCommand.java                        2.1 2024/16/11
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
 * 
 * Grammar:
 *          Do     ::= do Command while Expression 
 * @author: Wilfredo Villegas Méndez
 */

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class DoWhileCommand extends Command {

  public DoWhileCommand (Command cAST,Expression eAST,  SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C = cAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitDoWhileCommand(this, o);
  }

  public Expression E;
  public Command C;
}
