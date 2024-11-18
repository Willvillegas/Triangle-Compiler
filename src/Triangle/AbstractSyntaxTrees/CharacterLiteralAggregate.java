/*
 * @(#)CharacterLiteralAggregate.java                2.1 2024/17/11
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

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class CharacterLiteralAggregate extends CaseAggregate {

  public CharacterLiteralAggregate (CharacterLiteral clAST, Command cAST, CaseAggregate caAST, SourcePosition thePosition) {
    super (thePosition);
    CL = clAST;
    C = cAST;
    CA = caAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitCharacterLiteralAggregate(this, o);
  }

  public CharacterLiteral CL;
  public Command C;
  public CaseAggregate CA;
  public Integer jump;
}
