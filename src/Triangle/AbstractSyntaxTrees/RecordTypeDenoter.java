/*
 * @(#)RecordTypeDenoter.java                2.1 2003/10/07
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
import java.util.List;

public class RecordTypeDenoter extends TypeDenoter {

  public RecordTypeDenoter (FieldTypeDenoter ftAST, SourcePosition thePosition) {
    super (thePosition);
    FT = ftAST;
  }
  public RecordTypeDenoter (FieldTypeDenoter ftAST, List<FuncDeclaration> fdAST,List<ProcDeclaration> pdAST , SourcePosition thePosition) {
    super (thePosition);
    FT = ftAST;
    FD = fdAST;
    PD = pdAST;
    this.M = M;
  }

  public Object visit (Visitor v, Object o) {
    return v.visitRecordTypeDenoter(this, o);
  }

  public boolean equals (Object obj) {
    if (obj != null && obj instanceof ErrorTypeDenoter)
      return true;
    else if (obj != null && obj instanceof RecordTypeDenoter)
      return this.FT.equals(((RecordTypeDenoter) obj).FT);
    else
      return false;
  }
  public Declaration findMethod(Identifier methodName) {
    /*for (Declaration d : this.M) {
      if (d instanceof FuncDeclaration) {
        FuncDeclaration func = (FuncDeclaration) d;
        if (func.I.spelling.equals(methodName.spelling)) {
          return func;
        }
      } else if (d instanceof ProcDeclaration) {
        ProcDeclaration proc = (ProcDeclaration) d;
        if (proc.I.spelling.equals(methodName.spelling)) {
          return proc;
        }
      }
    }*/
    if(this.FD != null){
        for(FuncDeclaration d :this.FD){
            if (d.I.spelling.equals(methodName.spelling))
                return d;
        }
    }
    if (this.PD !=null){
        for(ProcDeclaration d :this.PD){
            if (d.I.spelling.equals(methodName.spelling))
                return d;
        }
    }
    return null;
  }

  public FieldTypeDenoter FT;
  public List<FuncDeclaration> FD;
  public List<ProcDeclaration> PD;
  public List<Declaration> M;
}
