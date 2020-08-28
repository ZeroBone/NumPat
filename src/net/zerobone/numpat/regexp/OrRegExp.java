package net.zerobone.numpat.regexp;

public class OrRegExp extends RegExp {

    private IRegExp left;
    private IRegExp right;

    public OrRegExp(IRegExp left, IRegExp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean nullable() {
        return left.nullable() || right.nullable();
    }

    @Override
    public boolean single() {
        return false;
    }

    @Override
    public StructureType getType() {
        return StructureType.TYPE_OR;
    }

    @Override
    public IRegExp removeRedundantEpsilon() {

        if (left == epsilon) {
            return right;
        }

        if (right == epsilon) {
            return left;
        }

        return new OrRegExp(left.removeRedundantEpsilon(), right.removeRedundantEpsilon());

    }

    @Override
    public IRegExp derive(int terminal) {
        return left.derive(terminal).orWith(right.derive(terminal));
    }

    @Override
    public void writeTo(StringBuilder sb) {
        left.writeTo(sb);
        sb.append('|');
        right.writeTo(sb);
    }

}