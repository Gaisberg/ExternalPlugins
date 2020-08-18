package net.runelite.client.plugins.externals.oneclick.Comparables;

import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;
import java.util.Collections;
import java.util.Set;

public class Herbs implements ClickComparable
{

	private static final Set<Integer> HERB_SET = Set.of(
			ItemID.GRIMY_RANARR_WEED, ItemID.GRIMY_TOADFLAX, ItemID.GRIMY_SNAPDRAGON,
			ItemID.LIMPWURT_ROOT
	);

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.NPC_FIRST_OPTION.getId() &&
				event.getOption().toLowerCase().contains("talk") &&
				event.getTarget().toLowerCase().contains("leprechaun") ||
				event.getOpcode() == MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId() &&
				event.getOption().toLowerCase().contains("inspect") &&
				event.getTarget().toLowerCase().contains("patch");
	}

	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{
		if (event.getOption().toLowerCase().contains("talk"))
		{
			if (plugin.findItem(HERB_SET).getLeft() == -1)
			{
				return;
			}
			event.setOption("Use");
			event.setTarget("<col=ff9040> " + plugin.findItemString(HERB_SET) + " <col=ffffff> -> " + event.getTarget());
			event.setForceLeftClick(true);
		}

		if (event.getOption().toLowerCase().contains("inspect"))
		{
			if (plugin.findItem(ItemID.BOTTOMLESS_COMPOST_BUCKET_22997).getLeft() == -1)
			{
				return;
			}
			event.setOption("Use");
			event.setTarget("<col=ff9040> " + plugin.findItemString(Collections.singleton(ItemID.BOTTOMLESS_COMPOST_BUCKET_22997)) + " <col=ffffff> -> " + event.getTarget());
			event.setForceLeftClick(true);
		}

	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		//Not yet implemented
		return false;
	}

	@Override
	public boolean isClickValid(OneClickPlugin plugin, MenuEntry event)
	{
		if (event.getTarget().toLowerCase().contains("leprechaun"))
		{
			return event.getOpcode() == MenuOpcode.NPC_FIRST_OPTION.getId() &&
					event.getTarget().contains("<col=ff9040> " + plugin.findItemString(HERB_SET) + " <col=ffffff> -> ") &&
					event.getTarget().toLowerCase().contains("leprechaun");
		}
		if (event.getTarget().toLowerCase().contains("patch"))
		{
			return event.getOpcode() == MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId() &&
					event.getTarget().contains("<col=ff9040> " + plugin.findItemString(Collections.singleton(ItemID.BOTTOMLESS_COMPOST_BUCKET_22997)) + " <col=ffffff> -> ") &&
					event.getTarget().toLowerCase().contains("patch");
		}
		return false;
	}

	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.updateSelectedItem(HERB_SET) && event.getTarget().toLowerCase().contains("leprechaun"))
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_NPC.getId());
			return;
		}
		if (plugin.updateSelectedItem(ItemID.BOTTOMLESS_COMPOST_BUCKET_22997) && event.getTarget().toLowerCase().contains("patch"))
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			return;
		}
	}
}
