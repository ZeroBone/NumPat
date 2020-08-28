package net.zerobone.numpat.regexp;

public abstract class RegExp implements IRegExp {

    protected RegExp() {}

    @Override
    public IRegExp removeRedundantEpsilon() {
        return this;
    }

    @Override
    public IRegExp concatWith(IRegExp other) {

        if (other == empty) {
            return empty;
        }

        return new ConcatRegExp(this, other);

    }

    @Override
    public IRegExp orWith(IRegExp other) {

        if (other == empty) {
            return this;
        }

        return new OrRegExp(this, other);

    }

    @Override
    public IRegExp repeat() {

        if (nullable()) {
            return new RepeatRegExp(removeRedundantEpsilon());
        }

        return new RepeatRegExp(this);

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        writeTo(sb);

        return sb.toString();

    }

    public static final IRegExp empty = new RegExp() {
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
            return StructureType.TYPE_EMPTY;
        }

        @Override
        public IRegExp derive(int terminal) {
            return empty;
        }

        @Override
        public IRegExp concatWith(IRegExp other) {
            return empty;
        }

        @Override
        public IRegExp orWith(IRegExp other) {
            return other;
        }

        @Override
        public IRegExp repeat() {
            return epsilon;
        }

        @Override
        public void writeTo(StringBuilder sb) {
            sb.append('∅');
        }
    };

    public static final IRegExp epsilon = new RegExp() {
        @Override
        public boolean nullable() {
            return true;
        }

        @Override
        public boolean single() {
            return true;
        }

        @Override
        public StructureType getType() {
            return StructureType.TYPE_EPSILON;
        }

        @Override
        public IRegExp derive(int terminal) {
            return empty;
        }

        @Override
        public IRegExp concatWith(IRegExp other) {
            return other;
        }

        @Override
        public IRegExp orWith(IRegExp other) {

            if (other.nullable()) {
                return other;
            }

            return super.orWith(other);

        }

        @Override
        public IRegExp repeat() {
            return epsilon;
        }

        @Override
        public void writeTo(StringBuilder sb) {
            sb.append('ε');
        }
    };

}