package net.zerobone.numpat.regexp;

public class TerminalRegExp extends RegExp {

    private int t;

    public TerminalRegExp(int terminal) {
        this.t = terminal;
    }

    @Override
    public boolean nullable() {
        return false;
    }

    @Override
    public boolean single() {
        return true;
    }

    @Override
    public StructureType getType() {
        return StructureType.TYPE_TERMINAL;
    }

    @Override
    public IRegExp derive(int terminal) {
        return t == terminal ? RegExp.epsilon : RegExp.empty;
    }

    @Override
    public void writeTo(StringBuilder sb) {
        sb.append((char)t);
    }

}