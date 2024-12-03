/*
 * @(#)CaseExpression.java                        2.1 2024/18/11
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
import java.util.Objects;
/**
 * CaseExpression 
 * Clase principal para la expresion Case en el Triangle Language
 * @author Wilfredo Villegas Mendez
 * 
 */
public class CaseExpression extends Expression {

  public CaseExpression (Vname vnAST,CaseAggregate caAST , SourcePosition thePosition) {
    super (thePosition);
    Vn = vnAST;
    Ca = caAST;
  }
  
  public Object visit(Visitor v, Object o) {
    return v.visitCaseExpression(this, o);
  }
  public Vname Vn;
  public CaseAggregate Ca;
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CaseExpression other = (CaseExpression) obj;
        if (!Objects.equals(this.Vn, other.Vn)) {
            return false;
        }
        return Objects.equals(this.Ca, other.Ca);
    }
  
}
