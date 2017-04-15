package es.egames.model;

import java.io.Serializable;

/**
 * Created by daniel on 2/02/17.
 */


public abstract class BaseEntity  implements Serializable {

    public BaseEntity() {

        super();
    }

    private int id;
    private int version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    @Override
    public int hashCode() {
        return (int) this.getId();
    }

    @Override
    public boolean equals(Object other) {
        boolean result;

        if (this == other)
            result = true;
        else if (other == null)
            result = false;
        else if (other instanceof Integer)
            result = (this.getId() == (Integer) other);
        else if (!this.getClass().isInstance(other))
            result = false;
        else
            result = (this.getId() == ((BaseEntity) other).getId());

        return result;
    }

}
