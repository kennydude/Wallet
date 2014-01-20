Wallet
======

A wallet for your cards and various things.

This app is a modular, easy-to-extend thing that can show loyalty cards or anything else you would like in your wallet.

All of the logos used should be covered under fair-usage and it really helps me tap the right card in a hurry :)

## How to build

1. Clone this repo
2. `git submodule init` `git submodule update` to pull in ORMDroid
3. Import into Android Studio and it should work*

* Android Studio hated ActionBar-PullToRefresh for me; It compiles though, just ignore it!

## How to add a custom card

1. Fork this repo
2. Create a new package name; I want it tidy!
3. Define 2 layouts:
   * An edit screen. This is where users input everything about their card. It's up to you to make this complete.
   * A card view: This must use `@drawable/card_background` or it'll look stupid
4. Write a subclass of `Card` (NOT `StoredCard`!)
5. You will need 2 activities explained bellow
6. Commit and send it back to me :)

### Edit Activity

This extends from ActivityEditCard. It does everything for you. If you want to do any validation
as you type, add it in `OnCreate`.

Just make sure you allow the activity to do it's thing, it's pretty good at it's job

### View Activity

This shows the contents of your card once tapped. This is usually a barcode generated, and a simple
`ActivityViewBarcode` is provided for you.
