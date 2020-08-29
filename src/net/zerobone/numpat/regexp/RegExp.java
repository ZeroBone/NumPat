package net.zerobone.numpat.regexp;

public abstract class RegExp implements IRegExp {

    protected RegExp() {}

    @Override
    public IRegExp concatWith(IRegExp other) {

        if (other.getType() == StructureType.TYPE_EMPTY) {
            return OrRegExp.emptySet();
        }

        if (other.getType() == StructureType.TYPE_EPSILON) {
            return this;
        }

        if (getType() != StructureType.TYPE_CONCAT && other.getType() == StructureType.TYPE_CONCAT) {
            return other.concatWith(this);
        }

        return new ConcatRegExp(this, other);

    }

    @Override
    public IRegExp orWith(IRegExp other) {

        if (other.getType() == StructureType.TYPE_EMPTY) {
            return this;
        }

        if (getType() != StructureType.TYPE_OR && other.getType() == StructureType.TYPE_OR) {
            return other.orWith(this);
        }

        return new OrRegExp(this, other);

    }

    @Override
    public IRegExp repeat() {
        return new RepeatRegExp(this);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        writeTo(sb);

        return sb.toString();

    }

}