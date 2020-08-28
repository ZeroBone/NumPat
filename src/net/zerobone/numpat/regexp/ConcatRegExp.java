package net.zerobone.numpat.regexp;

public class ConcatRegExp extends RegExp {

    private IRegExp left;
    private IRegExp right;

    public ConcatRegExp(IRegExp left, IRegExp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean nullable() {
        return left.nullable() && right.nullable();
    }

    @Override
    public boolean single() {
        return false;
    }

    @Override
    public StructureType getType() {
        return StructureType.TYPE_CONCAT;
    }

    @Override
    public IRegExp derive(int terminal) {

        IRegExp newLeft = left.derive(terminal).concatWith(right);

        if (left.nullable()) {
            // epsilon is then being concatenated on the right side

            return newLeft.orWith(right.derive(terminal));

        }

        return newLeft;

    }

    @Override
    public void writeTo(StringBuilder sb) {

        if (left.getType() == StructureType.TYPE_OR) {
            sb.append('(');
            left.writeTo(sb);
            sb.append(')');
        }
        else {
            left.writeTo(sb);
        }

        if (right.getType() == StructureType.TYPE_OR) {
            sb.append('(');
            right.writeTo(sb);
            sb.append(')');
        }
        else {
            right.writeTo(sb);
        }

    }
}