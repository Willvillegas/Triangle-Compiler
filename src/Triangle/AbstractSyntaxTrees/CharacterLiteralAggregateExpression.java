/*
 * @(#)CharacterLiteralAggregateExpression.java                2.1 2024/18/11
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

public class CharacterLiteralAggregateExpression extends CaseAggregateExpression {

  public CharacterLiteralAggregateExpression (CharacterLiteral clAST, Expression cAST, CaseAggregate caAST, SourcePosition thePosition) {
    super (thePosition);
    CL = clAST;
    E = cAST;
    CA = caAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitCharacterLiteralAggregateExpression(this, o);
  }

  public CharacterLiteral CL;
  public Expression E;
  public CaseAggregate CA;
  public Integer jump;
}
