package net.zerobone.numpat.regexp;

import java.util.ArrayList;

public class ConcatRegExp extends RegExp {

    private ArrayList<IRegExp> operands;

    private ConcatRegExp() {
        this.operands = new ArrayList<>(0);
    }

    public ConcatRegExp(ArrayList<IRegExp> operands) {
        this.operands = operands;
    }

    public ConcatRegExp(IRegExp left, IRegExp right) {

        operands = new ArrayList<>(2);

        operands.add(left);

        operands.add(right);

    }

    @Override
    public boolean nullable() {

        for (IRegExp re : operands) {
            if (!re.nullable()) {
                return false;
            }
        }

        return true;

    }

    private boolean isEpsilon() {
        return operands.isEmpty();
    }

    @Override
    public boolean single() {
        return operands.size() == 1 && operands.get(0).single();
    }

    @Override
    public IRegExp concatWith(IRegExp other) {

        if (isEpsilon()) {
            return other;
        }

        return super.concatWith(other);

    }

    @Override
    public IRegExp repeat() {

        if (isEpsilon()) {
            return this;
        }

        return super.repeat();

    }

    @Override
    public StructureType getType() {
        return isEpsilon() ? StructureType.TYPE_EPSILON : StructureType.TYPE_CONCAT;
    }

    @Override
    public void writeTo(StringBuilder sb) {

        if (isEpsilon()) {
            sb.append('ε');
            return;
        }

        for (IRegExp re : operands) {

            if (re.getType() == StructureType.TYPE_OR) {
                sb.append('(');
                re.writeTo(sb);
                sb.append(')');
            }
            else {
                re.writeTo(sb);
            }

        }

    }

    public static IRegExp epsilon() {
        return new ConcatRegExp();
    }

}