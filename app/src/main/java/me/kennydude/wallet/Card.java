package me.kennydude.wallet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Base class for a card.
 *
 * Defines generically what a card needs to provide.
 *
 * The only requirement is the card is GSON-serializable
 *
 * @author kennydude
 */
public abstract class Card {
	public Card(){}

	/**
	 * Return a view of a card
	 * @param inflater LayoutInflater for speed!
	 * @return View to show
	 */
	public abstract View getCardView(LayoutInflater inflater);

	/**
	 * Refresh Data
	 *
	 * YOU MUST RETURN FALSE ON ERROR! Otherwise invalid cards can be added
	 * and that is a bad user experience!
	 * @return true/false if successful
	 */
	public abstract boolean refreshData() throws Exception;

	/**
	 * Get the class required to edit/create cards
	 * @return Subclass of ActivityEditCard
	 */
	public abstract Class<? extends ActivityEditCard> getEditActivity();

	/**
	 * Get a class required to show details of a card
	 * @return Subclass of ActivityViewCard
	 */
	public abstract Class<? extends ActivityViewCard> getViewActivity();

}
