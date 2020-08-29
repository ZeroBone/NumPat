package net.zerobone.numpat.regexp;

public class RepeatRegExp extends RegExp {

    private IRegExp repeating;

    public RepeatRegExp(IRegExp repeating) {
        this.repeating = repeating;
    }

    @Override
    public boolean nullable() {
        return true;
    }

    @Override
    public boolean single() {
        return false;
    }

    @Override
    public IRegExp repeat() {
        return this;
    }

    @Override
    public StructureType getType() {
        return StructureType.TYPE_REPEAT;
    }

    @Override
    public void writeTo(StringBuilder sb) {

        if (repeating.single()) {
            repeating.writeTo(sb);
        }
        else {
            sb.append('(');
            repeating.writeTo(sb);
            sb.append(')');
        }

        sb.append('*');

    }

}