/*
 * @(#)IdEntry.java                        2.1 2003/10/07
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

package Triangle.ContextualAnalyzer;

import Triangle.AbstractSyntaxTrees.Declaration;
import Triangle.AbstractSyntaxTrees.FieldTypeDenoter;

/**
 * The IdEntry class is used to store the identifiers and their attributes
 * in the identification table.
 * The identification table is a stack of entries, each of which contains an identifier,
 * an attribute, a level, and a link to the previous entry at the same level.
 * The level is an integer that indicates the current level of the identification table.
 * The link is a reference to the previous entry at the same level.
 * The latest field is a reference to the most recent entry in the identification table.
 * 
 * Attributes:
 * - id: the identifier.
 * - attr: the attribute of the identifier.
 * - level: the current level of the identification table.
 * - previous: a reference to the previous entry at the same level.
 * 
 */

public class IdEntry {

  protected String id;
  protected Declaration attr;
  protected FieldTypeDenoter type;
  protected int level;
  protected IdEntry previous;

  IdEntry (String id, Declaration attr, int level, IdEntry previous) {
    this.id = id;
    this.attr = attr;
    this.type = null;
    this.level = level;
    this.previous = previous;
  }
  IdEntry (String id, FieldTypeDenoter type, int level, IdEntry previous) {
    this.id = id;
    this.attr = null;
    this.type = type;
    this.level = level;
    this.previous = previous;
  }

}
