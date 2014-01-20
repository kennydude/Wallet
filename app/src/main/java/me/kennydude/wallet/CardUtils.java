package me.kennydude.wallet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.roscopeco.ormdroid.Entity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.kennydude.wallet.clubcard.TescoClubcard;
import me.kennydude.wallet.subwayuk.SubwayCard;
import me.kennydude.wallet.testcard.TestCard;

/**
 * @author kennydude
 */
public class CardUtils {
	public static final String ACTION_NEW_CARD = "me.kennydude.wallet.NEW_CARD";
	public static final String ACTION_EDIT_CARD = "me.kennydude.wallet.EDIT_CARD";
	public static final String ACTION_CARD_REFRESHED = "me.kennydude.wallet.CARD_IS_RENEWED";
	public static List<CardDescription> cards = new ArrayList<CardDescription>();
	public static Map<String, Integer> cardIndexMap = new HashMap<String, Integer>();

	static{
		// Please ensure they are in alphabetical order!
		cards.add(new CardDescription(R.string.subway_card, SubwayCard.class, "subcarduk"));
		cards.add(new CardDescription(R.string.clubcard, TescoClubcard.class, "clubcarduk"));

		if(Utils.DEBUG){ // Test card!
			cards.add(new CardDescription(R.string.testcard, TestCard.class, "test"));
		}

		// This is required to make deserialization work!
		int i = 0;
		for(CardDescription cdr : cards){
			cardIndexMap.put(cdr.gsonType, i);
			i+=1;
		}
	}

	public static class CardDescription{
		public int stringId;
		public Class<? extends Card> cls;
		public String gsonType;

		public CardDescription(int stringId, Class<? extends  Card> cls, String gsonType){
			this.stringId = stringId;
			this.cls = cls;
			this.gsonType = gsonType;
		}
	}

	/**
	 * Card that is stored in the database!
	 *
	 * This is because of our inheritance. It doesn't really work well in a DB!
	 */
	public static class StoredCard extends Entity {
		public int id;
		public String data;

		public static StoredCard saveCard(Card cardToSave){
			StoredCard storedCard = new StoredCard();
			storedCard.setCard(cardToSave);
			// Store + Return
			storedCard.save();
			return storedCard;
		}

		public Card getCard(){
			GsonBuilder gb = new GsonBuilder();
			gb.registerTypeAdapter(Card.class, new CardDeserializer());

			return gb.create().fromJson(data, Card.class);
		}

		public void setCard(Card cardToSave){
			// Serialize
			GsonBuilder gb = new GsonBuilder();
			gb.registerTypeAdapter(Card.class, new CardSerializer());
			data = gb.create().toJson(cardToSave, Card.class);
		}
	}

	// Based on http://stackoverflow.com/questions/15578106/using-gson-to-parse-subclasses-with-different-fields
	public static class CardDeserializer implements JsonDeserializer<Card> {

		@Override
		public Card deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();

			// Get string type
			String strType = jsonObject.get("type").getAsString();

			// Now check if it exists
			if(!cardIndexMap.containsKey(strType)) throw new JsonParseException("Unexpected type");

			// If so ask GSON to deseralize with a specific class in mind
			return context.deserialize(jsonObject, cards.get( cardIndexMap.get(strType) ).cls);
		}

	}

	// Based on http://stackoverflow.com/questions/8153582/gson-doesnt-serialize-fields-defined-in-subclasses
	public static class CardSerializer implements JsonSerializer<Card> {

		@Override
		public JsonElement serialize(Card card, Type type, JsonSerializationContext context) {
			JsonObject ret = context.serialize(card).getAsJsonObject();

			String stype = null;
			for(CardDescription cdx : cards){
				if(cdx.cls.isInstance(card)){
					stype = cdx.gsonType;
				}
			}
			if(stype == null) return null; // FAIL

			JsonElement elx = context.serialize(stype);

			ret.add("type", elx);
			return ret;
		}
	}

}
