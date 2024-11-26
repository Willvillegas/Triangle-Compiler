/*
 * @(#)IdentificationTable.java                2.1 2003/10/07
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
/**
 * The IdentificationTable class is used to store the identifiers and their attributes
 * in the identification table. 
 * The identification table is a stack of entries, each of which contains an identifier,
 * an attribute, a level, and a link to the previous entry at the same level.
 * The level is an integer that indicates the current level of the identification table.
 * The link is a reference to the previous entry at the same level.
 * The latest field is a reference to the most recent entry in the identification table.
 * Attributes:
 * - level: the current level of the identification table.
 * - latest: a reference to the most recent entry in the identification table.
 * 
 * Methods:
 * - openScope: opens a new level in the identification table, 1 higher than the current topmost level.
 * - closeScope: closes the topmost level in the identification table, discarding all entries belonging to that level.
 * - enter: makes a new entry in the identification table for the given identifier and attribute.
 * - retrieve: finds an entry for the given identifier in the identification table, if any.
 * 
 * 
 * If there are several entries for that identifier, finds the entry at the highest level, in accordance with the scope rules.
 * Returns null iff no entry is found, otherwise returns the attribute field of the entry found.
 */
public final class IdentificationTable {

  private int level;
  private IdEntry latest;

  public IdentificationTable () {
    level = 0;
    latest = null;
  }

  // Opens a new level in the identification table, 1 higher than the
  // current topmost level.
  /**
   * En otras palabras, cuando esto se abre, las siguientes entradas o declaraciones,
   * serán de forma local, es decir, solo se podrán acceder en el mismo nivel.
   */
  public void openScope () {

    level ++;
  }

  // Closes the topmost level in the identification table, discarding
  // all entries belonging to that level.
  /**
   * En otras palabras, cuando esto se cierra, las siguientes entradas o declaraciones,
   * serán de forma global, es decir, se podrán acceder en cualquier nivel.
   */
  public void closeScope () {

    IdEntry entry, local;

    // Presumably, idTable.level > 0.
    entry = this.latest;
    while (entry.level == this.level) {
      local = entry;
      entry = local.previous;
    }
    this.level--;
    this.latest = entry;
  }

  // Makes a new entry in the identification table for the given identifier
  // and attribute. The new entry belongs to the current level.
  // duplicated is set to to true iff there is already an entry for the
  // same identifier at the current level.

  public void enter (String id, Declaration attr) {

    IdEntry entry = this.latest;
    boolean present = false, searching = true;

    // Check for duplicate entry ...
    while (searching) {
      if (entry == null || entry.level < this.level)
        searching = false;
      else if (entry.id.equals(id)) {
        present = true;
        searching = false;
       } else
       entry = entry.previous;
    }

    attr.duplicated = present;
    // Add new entry ...
    entry = new IdEntry(id, attr, this.level, this.latest);
    this.latest = entry;
  }

  // Finds an entry for the given identifier in the identification table,
  // if any. If there are several entries for that identifier, finds the
  // entry at the highest level, in accordance with the scope rules.
  // Returns null iff no entry is found.
  // otherwise returns the attribute field of the entry found.
  /**
   * Traducción:
   * Encuentra una entrada para el identificador dado en la tabla de identificación, si la hay
   * Si hay varias entradas para ese identificador, encuentra la entrada en el nivel más alto, de acuerdo con las reglas de alcance.
   * Devuelve nulo si no se encuentra ninguna entrada de lo contrario devuelve el campo de atributo de la entrada encontrada.
   * 
   */

  public Declaration retrieve (String id) {

    IdEntry entry;
    Declaration attr = null;
    boolean present = false, searching = true;

    entry = this.latest;
    while (searching) {
      if (entry == null)
        searching = false;
      else if (entry.id.equals(id)) {
        present = true;
        searching = false;
        attr = entry.attr;
      } else
        entry = entry.previous;
    }

    return attr;
  }

}
