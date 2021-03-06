package net.zerobone.numpat.regexp;

public interface IRegExp {

     boolean nullable();

     boolean single();

     StructureType getType();

     IRegExp concatWith(IRegExp other);

     IRegExp orWith(IRegExp other);

     IRegExp repeat();

     void writeTo(StringBuilder sb);

     String toString();

}