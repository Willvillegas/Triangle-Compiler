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
import java.util.Objects;

public class FuncTypeDenoter extends TypeDenoter {

  public FuncTypeDenoter (FuncRecordMethod frmAST, SourcePosition thePosition) {
    super (thePosition);
    this.RM=frmAST;
  }
  

  public Object visit (Visitor v, Object o) {
    return v.visitFuncTypeDenoter(this, o);
  }

  
  public FuncRecordMethod RM;

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
        final FuncTypeDenoter other = (FuncTypeDenoter) obj;
        return Objects.equals(this.RM, other.RM);
    }
  
}
