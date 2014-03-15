package jgile2.mods.projecte.api;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import jgile2.mods.projecte.lib.Compare;
import jgile2.mods.projecte.lib.LogHelper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class OreStack implements Comparable<OreStack>
{
    // Gson serializer for serializing to/deserializing from json
    private static final Gson gsonSerializer = new Gson();

    private static final int ORE_DICTIONARY_NOT_FOUND = -1;

    public String oreName;
    public int stackSize;

    public OreStack(String oreName, int stackSize)
    {
        this.oreName = oreName;
        this.stackSize = stackSize;
    }

    public OreStack(String oreName)
    {
        this(oreName, 1);
    }

    public OreStack(ItemStack itemStack)
    {
        this(OreDictionary.getOreName(OreDictionary.getOreID(itemStack)), itemStack.stackSize);
    }

    @Override
    public String toString()
    {
        return String.format("%sxoreStack.%s", stackSize, oreName);
    }

    @Override
    public boolean equals(Object object)
    {
        return object instanceof OreStack && (comparator.compare(this, (OreStack) object) == Compare.EQUALS);
    }

    public static boolean compareOreNames(OreStack oreStack1, OreStack oreStack2)
    {
        if (oreStack1 != null && oreStack2 != null)
        {
            if ((oreStack1.oreName != null) && (oreStack2.oreName != null))
            {
                return oreStack1.oreName.equalsIgnoreCase(oreStack2.oreName);
            }
        }

        return false;
    }

    @Override
    public int compareTo(OreStack oreStack)
    {
        return comparator.compare(this, oreStack);
    }

    /**
     * Deserializes a OreStack object from the given serialized json String
     *
     * @param jsonOreStack
     *         Json encoded String representing a OreStack object
     *
     * @return The OreStack that was encoded as json, or null if a valid OreStack could not be decoded from given String
     */
    @SuppressWarnings("unused")
    public static OreStack createFromJson(String jsonOreStack)
    {
        try
        {
            return gsonSerializer.fromJson(jsonOreStack, OreStack.class);
        }
        catch (JsonSyntaxException exception)
        {
            LogHelper.severe(exception.getMessage());
        }
        catch (JsonParseException exception)
        {
            LogHelper.severe(exception.getMessage());
        }

        return null;
    }

    /**
     * Returns this OreStack as a json serialized String
     *
     * @return Json serialized String of this OreStack
     */
    @SuppressWarnings("unused")
    public String toJson()
    {
        return gsonSerializer.toJson(this);
    }

    public static OreStack getOreStackFromList(Object... objects)
    {
        return getOreStackFromList(Arrays.asList(objects));
    }

    public static OreStack getOreStackFromList(List<?> objectList)
    {
        for (Object listElement : objectList)
        {
            if (listElement instanceof ItemStack)
            {
                ItemStack stack = (ItemStack) listElement;

                if (OreDictionary.getOreID(stack) != ORE_DICTIONARY_NOT_FOUND)
                {
                    return new OreStack(stack);
                }
            }
        }

        return null;
    }

    public static int compare(OreStack oreStack1, OreStack oreStack2)
    {
        return comparator.compare(oreStack1, oreStack2);
    }

    public static Comparator<OreStack> comparator = new Comparator<OreStack>()
    {

        @Override
        public int compare(OreStack oreStack1, OreStack oreStack2)
        {

            if (oreStack1 != null)
            {
                if (oreStack2 != null)
                {
                    if (oreStack1.oreName.equalsIgnoreCase(oreStack2.oreName))
                    {
                        return oreStack1.stackSize - oreStack2.stackSize;
                    }
                    else
                    {
                        return oreStack1.oreName.compareToIgnoreCase(oreStack2.oreName);
                    }
                }
                else
                {
                    return Compare.LESSER_THAN;
                }
            }
            else
            {
                if (oreStack2 != null)
                {
                    return Compare.GREATER_THAN;
                }
                else
                {
                    return Compare.EQUALS;
                }
            }
        }
    };
}
