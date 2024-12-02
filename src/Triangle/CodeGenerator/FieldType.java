/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Triangle.CodeGenerator;

import java.util.Objects;

/**
 * FieldType
 * Esta clase trabaja como un empaquetador del offset de los TypeDenoters(variables) y de los Frames(para funciones y proc)
 * 
 * @author Wilfredo Villegas Méndez
 */
public class FieldType {
   public Integer offsettype; 
   public Frame frame;

    public FieldType(Integer offsettype, Frame frame) {
        this.offsettype = offsettype;
        this.frame = frame;
    }

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
        final FieldType other = (FieldType) obj;
        if (!Objects.equals(this.offsettype, other.offsettype)) {
            return false;
        }
        return Objects.equals(this.frame, other.frame);
    }
   
   
}
