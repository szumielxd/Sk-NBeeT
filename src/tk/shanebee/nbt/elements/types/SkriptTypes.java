package tk.shanebee.nbt.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import tk.shanebee.nbt.NBeeT;
import tk.shanebee.nbt.elements.objects.Bound;
import tk.shanebee.nbt.elements.objects.OldBound;

import java.io.StreamCorruptedException;

@SuppressWarnings({"deprecation", "unused"})
public class SkriptTypes {

    static {
        Classes.registerClass(new ClassInfo<>(OldBound.class, "oldbound")
                .user("oldbound")
                .name("Old Bounding Box - Deprecated")
                .description("Represents a 3D bounding box between 2 points. This is deprecated and no longer used.")
                .defaultExpression(new EventValueExpression<>(OldBound.class))
                .since("2.6.0-Deprecated")
                .serializer(new Serializer<OldBound>() {
                    @Override
                    public Fields serialize(OldBound bound) {
                        Fields fields = new Fields();
                        fields.putObject("bound", bound.toString());
                        return fields;
                    }

                    @Override
                    public void deserialize(OldBound bound, Fields fields) {
                        assert false;
                    }

                    @Override
                    public OldBound deserialize(String s) {
                        return new OldBound(s);
                    }

                    @Override
                    protected OldBound deserialize(Fields fields) throws StreamCorruptedException {
                        String bound = fields.getObject("bound", String.class);
                        return new OldBound(bound);
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }

                }));
        Classes.registerClass(new ClassInfo<>(Bound.class, "bound")
                .user("bound")
                .name("Bound")
                .description("Represents a 3D bounding box between 2 points")
                .defaultExpression(new EventValueExpression<>(Bound.class))
                .since("2.8.0")
                .serializer(new Serializer<Bound>() {
                    @Override
                    public Fields serialize(Bound bound) {
                        Fields fields = new Fields();
                        fields.putObject("boundID", bound.getId());
                        return fields;
                    }

                    @Override
                    public void deserialize(Bound bound, Fields fields) {
                        assert false;
                    }

                    @Override
                    public Bound deserialize(String s) {
                        return null;
                    }

                    @Override
                    protected Bound deserialize(Fields fields) throws StreamCorruptedException {
                        String bound = fields.getObject("boundID", String.class);
                        return NBeeT.getInstance().getBoundConfig().getBoundFromID(bound);
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }

                }));
    }

}
