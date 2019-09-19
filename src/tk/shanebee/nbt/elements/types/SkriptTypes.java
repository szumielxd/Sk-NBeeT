package tk.shanebee.nbt.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import tk.shanebee.nbt.elements.objects.Bound;

import java.io.StreamCorruptedException;

@SuppressWarnings("deprecation")
public class SkriptTypes {

    static {
        Classes.registerClass(new ClassInfo<>(Bound.class, "bound")
                .name("Bounding Box")
                .description("Represents a 3D bounding box between 2 points")
                .since("2.6.0")
                .serializer(new Serializer<Bound>() {
                    @Override
                    public Fields serialize(Bound bound) {
                        Fields fields = new Fields();
                        fields.putObject("bound", bound.toString());
                        return fields;
                    }

                    @Override
                    public void deserialize(Bound bound, Fields fields) {
                        assert false;
                    }

                    @Override
                    public Bound deserialize(String s) {
                        return new Bound(s);
                    }

                    @Override
                    protected Bound deserialize(Fields fields) throws StreamCorruptedException {
                        String bound = fields.getObject("bound", String.class);
                        return new Bound(bound);
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
