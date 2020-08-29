package net.zerobone.numpat.regexp;

import java.util.ArrayList;
import java.util.Iterator;

public class OrRegExp extends RegExp {

    private ArrayList<IRegExp> operands;

    private OrRegExp() {
        this.operands = new ArrayList<>(0);
    }

    public OrRegExp(ArrayList<IRegExp> operands) {
        this.operands = operands;
    }

    public OrRegExp(IRegExp left, IRegExp right) {

        operands = new ArrayList<>(2);

        operands.add(left);

        operands.add(right);

    }

    @Override
    public boolean nullable() {

        for (IRegExp re : operands) {
            if (re.nullable()) {
                return true;
            }
        }

        return false;

    }

    private boolean isEmptySet() {
        return operands.isEmpty();
    }

    @Override
    public boolean single() {
        return isEmptySet() || operands.size() == 1;
    }

    @Override
    public StructureType getType() {
        return isEmptySet() ? StructureType.TYPE_EMPTY : StructureType.TYPE_OR;
    }

    @Override
    public IRegExp concatWith(IRegExp other) {

        if (isEmptySet()) {
            return other;
        }

        return super.concatWith(other);

    }

    @Override
    public IRegExp orWith(IRegExp other) {

        if (other.getType() == StructureType.TYPE_EMPTY) {
            return this;
        }

        if (isEmptySet()) {
            return other;
        }

        ArrayList<IRegExp> ops = new ArrayList<>(operands);

        if (other.getType() == StructureType.TYPE_OR) {
            ops.addAll(((OrRegExp)other).operands);
        }
        else {
            ops.add(other);
        }

        return new OrRegExp(ops);

    }

    @Override
    public IRegExp repeat() {

        if (isEmptySet()) {
            return ConcatRegExp.epsilon();
        }

        for (IRegExp re : operands) {
            if (re.getType() == StructureType.TYPE_EPSILON) {
                return new RepeatRegExp(removeRedundantEpsilon());
            }
        }

        return super.repeat();

    }

    private IRegExp removeRedundantEpsilon() {

        ArrayList<IRegExp> ops = new ArrayList<>();

        for (IRegExp re : operands) {

            if (re.getType() == StructureType.TYPE_EPSILON) {
                continue;
            }

            ops.add(re);

        }

        return new OrRegExp(ops);

    }

    @Override
    public void writeTo(StringBuilder sb) {

        if (isEmptySet()) {
            sb.append('∅');
            return;
        }

        Iterator<IRegExp> it = operands.iterator();

        assert it.hasNext();

        while (true) {

            IRegExp re = it.next();

            re.writeTo(sb);

            if (!it.hasNext()) {
                return;
            }

            sb.append('|');

        }

    }

    public static IRegExp emptySet() {
        return new OrRegExp();
    }

}