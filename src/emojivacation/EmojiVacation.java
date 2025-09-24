package emojivacation;


import edu.macalester.graphics.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("SameParameterValue")
public class EmojiVacation {
    private static final Color
        SUN_YELLOW = new Color(0xffff78),
        SUN_BORDER_YELLOW = new Color(0xdcdc3c),
        SKY_BLUE = new Color(0xccd9ff),
        CLOUD_COLOR = new Color(0x80ffffff, true),
        TREE_TRUNK_COLOR = new Color(0x553511),
        TREE_LEAVES_COLOR = new Color(0x17af13),
        GRASS_COLOR = new Color(0xbcda9f),
        MOUNTAIN_COLOR = new Color(0x769afe),
        NO_SLIDE_COLOR = new Color(0x22211a);

    private static final int
        SCENE_WIDTH = 800,
        SCENE_HEIGHT = 600;

    private static Random random = new Random();

    public static void main(String[] args) {
        CanvasWindow canvas = new CanvasWindow("Emoji Family Vacation", SCENE_WIDTH, SCENE_HEIGHT);
        doSlideShow(canvas);
    }

    private static void doSlideShow(CanvasWindow canvas) {
        while (true) {
        //  Change this to an actual slideshow
        generateVacationPhoto(canvas);
        canvas.draw();
        canvas.pause(3000);
        canvas.removeAll();
        }
    }

    private static void generateVacationPhoto(CanvasWindow canvas) {
        canvas.setBackground(randomColorVariation(SKY_BLUE, 8));

        addSun(canvas);

        addCloudRows(canvas);

        // Create mountains 50% of the time.
        if (percentChance(50)) {
            double size = randomDouble(SCENE_WIDTH, SCENE_HEIGHT);
            int layers = randomInt (1, 3);
            addMountains(canvas, 400, size, layers);
        }
        addGround(canvas, 400);

        // Create forests 60% of the time. You should randomly
        if(percentChance(60)){
            int count = randomInt(6,14);
            addForest(canvas,350,120,count);
        }

        List<GraphicsGroup> family = createFamily(2, 3);
        positionFamily(family, 60, 550, 20);
       
        // Add each emoji in the list to the canvas
        for (GraphicsGroup person : family) {
            canvas.add(person);
        }
    }

    // –––––– Emoji family –––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––

    private static List<GraphicsGroup> createFamily(int adultCount, int childCount) {
        double adultSize = 160, childSize = 90;
        List<GraphicsGroup> family = new ArrayList<>();
        for (int i = 0; i < adultCount; i++) {
            family.add(createRandomEmoji(adultSize));   
        }
        for (int i = 0; i < childCount; i++) {
            family.add(createRandomEmoji(childSize));
        }
        return family;
    }

    private static GraphicsGroup createRandomEmoji(double size) {
        // Change this so that instead of always creating a smiley face,
        int pick = randomInt(0,4);
        switch (pick) {
            case 0: return ProvidedEmojis.createSmileyFace(size);
            case 1: return ProvidedEmojis.createContentedFace(size);
            case 2: return ProvidedEmojis.createFrownyFace(size);
            case 3: return ProvidedEmojis.createNauseousFace(size);
            case 4: return ProvidedEmojis.createWinkingFace(size);
            default: return ProvidedEmojis.createSmileyFace(size);
        }
    }

    private static void positionFamily(
            List<GraphicsGroup> family,
            double leftX,
            double baselineY,
            double spacing
    ) {
        double x = leftX;
        for (GraphicsGroup person : family) {
            double y = baselineY - person.getHeight();
            person.setPosition(x,y);
            x += person.getWidth() + spacing;
        }
    }

    // –––––– Scenery ––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––

    /**
     * Fills the bottom of the screen with a solid color. Even emojis need to stand somewhere!
     *
     * @param horizonY The top of the “ground” rectangle.
     */
    private static void addGround(CanvasWindow canvas, double horizonY) {
        Rectangle ground = new Rectangle(
            0, horizonY,
            SCENE_WIDTH, SCENE_HEIGHT - horizonY);
        ground.setFillColor(randomColorVariation(GRASS_COLOR, 32));
        ground.setFilled(true);
        ground.setStroked(false);
        canvas.add(ground);
    }

    /**
     * Creates a mountain range.
     *
     * @param baseY  The vertical position of the foot of the mountains
     * @param size   The height of each layer of mountains, and width of each triangle
     * @param layers The number of layers of mountain ranges to create
     */
    private static void addMountains(CanvasWindow canvas, double baseY, double size, int layers) {
        for (int layer = layers - 1; layer >= 0; layer--) {
            canvas.add(createLayerOfMountains(baseY - layer * size * 0.2, size));
        }
    }

    /**
     * Creates one layer of a mountain range.
     * @param layerBaseY The position of the feet of the mountains
     * @param size The maximum height of the peaks
     */
    private static GraphicsGroup createLayerOfMountains(double layerBaseY, double size) {
        GraphicsGroup group = new GraphicsGroup();

        double layerLeft = randomDouble(-size, 0);
        double layerRight = SCENE_WIDTH + size;
        Color layerColor = randomColorVariation(MOUNTAIN_COLOR, 16);

        double x = layerLeft;
        while (x < layerRight) {
            double curHeight = randomDouble(size * 0.4, size),
                curWidth = curHeight * randomDouble(1.0, 1.6);
            Path peak = Path.makeTriangle(
                x - curWidth, layerBaseY,
                x, layerBaseY - curHeight,
                x + curWidth, layerBaseY);
            peak.setFillColor(layerColor);
            peak.setFilled(true);
            peak.setStroked(false);
            group.add(peak);
            x += curWidth * 0.5;
        }
        return group;
    }

    /**
     * Creates many trees spanning the full width of the screen.
     *
     * @param baseY Vertical position of the highest tree’s tree
     * @param ySpan Vertical distance spanned by the tree trunks’ bases
     * @param count Number of trees
     */
    private static void addForest(CanvasWindow canvas, double baseY, double ySpan, int count) {
        for (int n = 0; n < count; n++) {
            GraphicsGroup tree = createTree(80, 90);
            tree.setPosition(
                randomDouble(0, SCENE_WIDTH),
                baseY + n * ySpan / count);
            canvas.add(tree);
        }
    }

    /**
     * Creates a tree with a brown trunk and idyllic green leaves.
     *
     * @param trunkHeight  The distance from the trunk’s base to the center of the leaves
     * @param leavesSize   The width and height of the cluster of leaves
     */
    private static GraphicsGroup createTree(double trunkHeight, double leavesSize) {
        GraphicsGroup group = new GraphicsGroup();

        Color trunkColor = randomColorVariation(TREE_TRUNK_COLOR, 8);
        double trunkWidth = trunkHeight * 0.2;

        Rectangle trunk = new Rectangle(
            -trunkWidth / 2, -trunkHeight,
            trunkWidth, trunkHeight);
        trunk.setFillColor(trunkColor);
        trunk.setFilled(true);
        trunk.setStroked(false);
        group.add(trunk);

        // A little roundness at the bottom of the trunk
        double baseEllipseHeight = trunkWidth * 0.25;
        Ellipse trunkBase = new Ellipse(
            -trunkWidth / 2, -baseEllipseHeight / 2,
            trunkWidth, baseEllipseHeight);
        trunkBase.setFillColor(trunkColor);
        trunkBase.setFilled(true);
        trunkBase.setStroked(false);
        group.add(trunkBase);

        GraphicsGroup treeTop = createPuff(
            leavesSize, leavesSize,
            false,
            leavesSize * 0.2,
            randomColorVariation(TREE_LEAVES_COLOR, 16));
        treeTop.setPosition(0, -trunkHeight);
        group.add(treeTop);

        return group;
    }

    private static void addSun(CanvasWindow canvas) {
        GraphicsGroup sun = createSun(randomDouble(30, 50), randomInt(8, 24));
        sun.setCenter(randomDouble(100, 700), randomDouble(60, 200));
        canvas.add(sun);
    }

    /**
     * Puts the sun in your sky.
     *
     * @param radius   Radius of the sun’s inner circle, not including rays.
     * @param rayCount The number of rays emanating from the sun. May be 0.
     */
    private static GraphicsGroup createSun(double radius, int rayCount) {
        GraphicsGroup sun = new GraphicsGroup();

        Ellipse sunCenter = new Ellipse(
            -radius, -radius,
            radius * 2, radius * 2);
        sunCenter.setFillColor(SUN_YELLOW);
        sunCenter.setFilled(true);
        sunCenter.setStrokeColor(SUN_BORDER_YELLOW);
        sunCenter.setStrokeWidth(3);
        sun.add(sunCenter);

        addSunRays(sun, radius * 1.2, radius * 1.4, rayCount);
        return sun;
    }

    /**
     * Draws the rays around the sun. Adds the rays to the given graphics group.
     */
    private static void addSunRays(
            GraphicsGroup sun,
            double innerRadius,
            double outerRadius,
            int rayCount
    ) {
        for (int n = 0; n < rayCount; n++) {
            double theta = Math.PI * 2 * n / rayCount;
            double dx = Math.cos(theta),
                   dy = Math.sin(theta);
            Line ray = new Line(
                dx * innerRadius, dy * innerRadius,
                dx * outerRadius, dy * outerRadius);
            ray.setStrokeWidth(3);
            ray.setStrokeColor(SUN_YELLOW);
            sun.add(ray);
        }
    }

    /**
     * Creates clouds of a random size and puffiness, spaced in neat rows and scattered horizontally.
     */
    private static void addCloudRows(CanvasWindow canvas) {
        double cloudRowSize = randomDouble(20, 120);
        double cloudPuffSize = randomDouble(20, 40);
        for (double y = 0; y < SCENE_HEIGHT; y += cloudRowSize) {
            GraphicsGroup cloud = createPuff(
                randomDouble(100, 200), cloudRowSize * 0.6,
                true,
                cloudPuffSize,
                CLOUD_COLOR);
            cloud.setPosition(randomDouble(0, 800), y);
            canvas.add(cloud);
        }
    }

    /**
     * Creates a clump of overlapping circles. Useful for clouds and treetops.
     *
     * The whole puff is centered horizontally at x=0, and the “baseline” of the puff is at y=0
     * (see the flatBottom parameter).
     *
     * @param width      The approximate width (with random variation) spanned by the centers of the
     *                   cloud’s puffs. This does not include the size of the individual puffs.
     * @param height     The approximate extent of the puff centers above the cloud’s baseline
     * @param flatBottom True = semicircle (with bottom on baseline);
     *                   false = full circle (centered on baseline)
     * @param puffSize   The size of circles that make up the cloud, with random variation.
     * @return           A group containing the whole puff.
     */
    private static GraphicsGroup createPuff(
            double width, double height,
            boolean flatBottom,
            double puffSize,
            Color puffColor
    ) {
        GraphicsGroup group = new GraphicsGroup();
        double maxTheta;
        if (flatBottom) {
            maxTheta = Math.PI;
        } else {
            maxTheta = Math.PI * 2;
        }
        double puffDensity = 0.3;
        int puffCount = (int) Math.ceil(Math.PI * width * height / puffSize * puffDensity);
        for (int puffNum = 0; puffNum < puffCount; puffNum++) {
            double theta = randomDouble(0, maxTheta);
            double r = randomDouble(0, 1);
            double curPuffSize = puffSize * randomDouble(0.5, 1.5);
            Ellipse puff = new Ellipse(
                -curPuffSize / 2 + Math.cos(theta) * r * width / 2,
                -curPuffSize / 2 - Math.sin(theta) * r * height / 2,
                curPuffSize, curPuffSize);
            puff.setFillColor(randomColorVariation(puffColor, 3));
            puff.setFilled(true);
            puff.setStroked(false);
            group.add(puff);
        }
        return group;
    }

    // –––––– Randomness helpers –––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––

    /**
     * Convenience to return a random floating point number, min ≤ n < max.
     */
    private static double randomDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    /**
     * Convenience to return a random integer, min ≤ n ≤ max.
     * Note that max is inclusive.
     */
    private static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Convenience to return true with the given percent change (0 = always false, 100 = always true).
     */
    private static boolean percentChance(double chance) {
        return random.nextDouble() * 100 < chance;
    }

    /**
     * Returns a slightly different color than the given one. Useful for making a bunch of items not
     * look entirely identical.
     */
    private static Color randomColorVariation(Color color, int amount) {
        return new Color(
            colorChannelVariation(color.getRed(), amount),
            colorChannelVariation(color.getGreen(), amount),
            colorChannelVariation(color.getBlue(), amount),
            color.getAlpha());
    }

    /**
     * Varies the given value randomly, pinned to [0...255].
     */
    private static int colorChannelVariation(int c, int amount) {
        return Math.min(255, Math.max(0, c + randomInt(-amount, amount)));
    }
}
