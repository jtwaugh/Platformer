package resource;

import java.util.HashMap;

public class KeywordTable
{
	private HashMap<String, Integer> keywords;
	
	public KeywordTable()
	{
		keywords = new HashMap<String, Integer>();
		keywords.put("MessageBox", 		1);
		keywords.put("name", 			2);
		keywords.put("width", 			3);
		keywords.put("height", 			4);
		keywords.put("style", 			5);
		keywords.put("text", 			6);
		keywords.put("Event", 			7);
		keywords.put("trigger", 		8);
		keywords.put("gameLoad", 		9);
		keywords.put("effect", 			10);
		keywords.put("Window", 			11);
		keywords.put("member", 			12);
		keywords.put("display", 		13);
		keywords.put("close", 			14);
		keywords.put("player", 			15);
		keywords.put("lowHP", 			16);
		keywords.put("Character",		17);
		keywords.put("sprite",			18);
		keywords.put("weight",			19);
		keywords.put("speed",			20);
		keywords.put("hp",				21);
		keywords.put("pos",				22);
		keywords.put("playerTalk",		23);
		keywords.put("Level",			24);
		keywords.put("currentMap",		25);
		keywords.put("ChoiceInterface",	26);
		keywords.put("options",			27);
		keywords.put("choiceStyle",		28);
		keywords.put("exitString",		29);
		keywords.put("graphic",			30);
		keywords.put("reward",			31);
		keywords.put("power",			32);
		keywords.put("water",			33);
		keywords.put("Mission",			34);
		keywords.put("Region",			35);
		keywords.put("population",		36);
		keywords.put("industry",		37);
		keywords.put("food",			38);
		keywords.put("falldamage",		39);
		keywords.put("complete",		40);
		keywords.put("requirements",	41);
		keywords.put("liberate",		42);
		keywords.put("Timer",			43);
		keywords.put("duration",		44);
		keywords.put("start",			45);
		keywords.put("end",				46);
		keywords.put("RandMission",		47);
		keywords.put("foodRiot",		48);
		keywords.put("waterRiot",		49);
		keywords.put("powerRiot",		50);
		keywords.put("greedRiot",		51);
		keywords.put("ladder",			52);
		keywords.put("transform",		53);
		keywords.put("Events",			54);
		keywords.put("ai",		 		55);
		keywords.put("backdrop",  		56);
		keywords.put("proximity", 		57);
		keywords.put("x", 				58);
		keywords.put("y", 				59);
		
	}
	
	public int hash(String term)
	{
		try
		{
			return keywords.get(term);
		}
		catch (Exception e)
		{
			System.out.println();
			return -1;
		}
	}
}
