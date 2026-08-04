"""Insert the crystal-bag self-check into ACPortSelfCheck.

Kept as a script rather than an inline heredoc: the Java contains nested quotes that a shell
heredoc mangles.
"""
import pathlib

CHECK = '''    /**
     * Exercises the crystal bag and the Materializer's crystal accounting.
     * <p>
     * The 1.12.2 consumeCrystals assigned its working copy back to a parameter, which does nothing
     * to the caller, so a recipe that could only be partially paid still spent crystals. This
     * asserts the opposite: a check spends nothing, and an unaffordable materialization leaves the
     * bag untouched.
     */
    private static void checkCrystalBag(List<String> problems) {
        MaterializerRecipes recipes = MaterializerRecipes.instance();

        ItemStack bag = new ItemStack(ACItems.small_crystal_bag.get());
        recipes.replaceBagContents(bag, new ItemStack[]{new ItemStack(ACItems.crystal(0), 4)});

        ItemStack[] contents = recipes.extractItemsFromBag(bag);
        if (contents == null || contents.length != 1 || contents[0].getCount() != 4) {
            problems.add("crystal bag did not round-trip its contents");
            return;
        }

        if (!recipes.canConsume(contents, new ItemStack[]{new ItemStack(ACItems.crystal(0), 3)})) {
            problems.add("crystal bag refused a recipe it can afford");
        }
        if (recipes.canConsume(contents, new ItemStack[]{new ItemStack(ACItems.crystal(0), 9)})) {
            problems.add("crystal bag accepted a recipe it cannot afford");
        }

        ItemStack[] afterChecks = recipes.extractItemsFromBag(bag);
        if (afterChecks == null || afterChecks[0].getCount() != 4) {
            problems.add("crystal bag lost crystals to a non-spending check");
        }

        // A materialization the bag cannot pay for must spend nothing at all.
        recipes.processMaterialization(new ItemStack(ACItems.crystal(1)), bag);
        ItemStack[] afterFailedSpend = recipes.extractItemsFromBag(bag);
        if (afterFailedSpend == null || afterFailedSpend[0].getCount() != 4) {
            problems.add("crystal bag spent crystals on a materialization it could not complete");
        }
    }

'''

path = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/common/util/ACPortSelfCheck.java")
text = path.read_text(encoding="utf-8")

text = text.replace(
    "        checkCapabilityRegistered(problems);",
    "        checkCapabilityRegistered(problems);\n        checkCrystalBag(problems);",
    1,
)
text = text.replace(
    "    private static int checkBlocks(List<String> problems) {",
    CHECK + "    private static int checkBlocks(List<String> problems) {",
    1,
)
text = text.replace(
    "import com.shinoow.abyssalcraft.api.recipe.EngraverRecipes;",
    "import com.shinoow.abyssalcraft.api.recipe.EngraverRecipes;\n"
    "import com.shinoow.abyssalcraft.api.recipe.MaterializerRecipes;",
    1,
)

path.write_text(text, encoding="utf-8")
print("inserted crystal bag check")
