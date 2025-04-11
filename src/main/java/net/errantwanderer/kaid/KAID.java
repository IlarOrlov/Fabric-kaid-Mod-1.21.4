package net.errantwanderer.kaid;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KAID implements ModInitializer {
	private static MinecraftServer server;
	private static final Random RANDOM = new Random();
	private static boolean killAllEnabled = true; // Toggle mode
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private static final String[] MESSAGES = {
			"\uD83D\uDC80 {player} made a fatal mistake! Now everyone suffers! \uD83D\uDC80",
			"\uD83D\uDD25 {player} tripped over a pixel. Time to restart the world! \uD83D\uDD25",
			"⚠ Oops! {player} wasn't supposed to do that...",
			"\uD83C\uDFAD {player} just ended the server with a skill issue!",
			"\uD83E\uDEA6 RIP {player}. Hope you had fun!",
			"\uD83C\uDF87 {player} turned into fireworks. Everyone joins the party!",
			"\uD83D\uDCAD {player} activated the 'no survivors' rule!",
			"\uD83D\uDE08 {player} angered the Minecraft gods. Now, everyone pays the price!",
			"\uD83D\uDCA5 {player} was too dangerous to be left alive. So, we took out everyone!",
			"\uD83D\uDCA8 {player} sneezed too hard and created a global extinction event!",
			"\uD83D\uDC40 {player} blinked. The universe didn't like that...",
			"\uD83D\uDCA6 {player} drowned in their own mistakes—taking everyone with them!",
			"\uD83D\uDC7B {player} became a ghost and decided misery loves company!",
			"\uD83D\uDE80 {player} tried speedrunning life... and failed.",
			"\uD83C\uDF0C {player} divided by zero. Reality crashed.",
			"\uD83E\uDD16 {player} was an AI experiment gone wrong. Server rebooting...",
			"\uD83E\uDD5E {player} stepped on a Lego and didn't survive. Neither did anyone else.",
			"\uD83C\uDF1F {player} made a wish upon a shooting star... and deleted existence.",
			"\uD83D\uDC7E {player} called the aliens, but they misunderstood the message.",
			"\uD83C\uDF0B {player} summoned a volcano. Whoops.",
			"\uD83D\uDD2A {player} thought PvP was off. It wasn’t.",
			"\uD83C\uDFB6 {player} played a sad song on the world’s smallest violin... and crashed reality.",
			"\uD83C\uDF0E {player} clicked something they really shouldn't have.",
			"\uD83E\uDD37 {player} asked 'what's the worst that could happen?' We found out.",
			"\uD83D\uDEAB {player} broke the tutorial. Game over.",
			"\uD83C\uDF1A {player} took 'fall damage'... from bedrock level.",
			"\uD83E\uDD14 {player} tried to eat TNT. It didn’t end well.",
			"\uD83E\uDD21 {player} tried a redstone experiment. The world is now soup.",
			"\uD83C\uDF2A {player} summoned a weather event labeled 'apocalypse'.",
			"\uD83C\uDFC3\u200D♂️ {player} ran into a wall. The wall won.",
			"\uD83E\uDD26 {player} opened a suspicious file named 'server_crash_final.exe'.",
			"\uD83D\uDC68\u200D\uD83D\uDCBB {player} copy-pasted from StackOverflow. Fatal exception!",
			"\uD83C\uDF10 {player} attempted to connect to the multiverse. It connected back.",
			"\uD83E\uDD20 {player} challenged the Ender Dragon to 1v1 IRL. Mistake.",
			"\uD83E\uDE96 {player} tried to pet a creeper. Spoiler: It wasn’t a cat.",
			"\uD83D\uDE44 {player} stared at an Enderman... and blinked first.",
			"\uD83D\uDEE0️ {player} was debugging with fire. Literally.",
			"\uD83D\uDCA3 {player} placed TNT to 'light up the area'. Mission accomplished.",
			"\uD83E\uDD28 {player} thought lava was orange juice.",
			"\uD83E\uDD22 {player} licked a command block. Now we all regret it.",
			"\uD83E\uDD0D {player} looked too deep into the void. The void looked back.",
			"\uD83E\uDD37 {player} said 'Trust me'. Famous last words.",
			"\uD83D\uDC7D {player} tried to negotiate with a creeper. The creeper said 'Boom'.",
			"\uD83C\uDF0A {player} fell into water. Missed it by *that* much.",
			"\uD83C\uDFA9 {player} wore the cursed hat. Then everything exploded.",
			"\uD83D\uDE3A {player} meowed at a Warden. Bad call.",
			"\uD83E\uDD2F {player} jumped into a portal labeled '???'.",
			"\uD83E\uDD14 {player} asked if the cake was a lie. The server couldn’t handle the truth.",
			"\uD83D\uDCAC {player} said 'brb'... never came back.",
			"\uD83E\uDD2F {player} challenged physics to a duel and lost.",
			"\uD83C\uDF89 {player} triggered a surprise party. With TNT.",
			"\uD83E\uDEE0 {player} played God. God played back.",
			"\uD83C\uDF10 {player} pressed Alt+F4 IRL. Server agreed.",
			"\uD83C\uDFC3 {player} tried to run from fate. Fate was faster.",
			"\uD83D\uDCCA {player} failed basic survival math.",
			"\uD83E\uDD76 {player} got cold feet. Then everyone froze.",
			"\uD83D\uDCA9 {player} slipped on a banana. Mass extinction ensued.",
			"\uD83D\uDE97 {player} jaywalked in the Nether. Big mistake.",
			"\uD83C\uDFAF {player} missed their shot. With a nuke.",
			"\uD83D\uDC96 {player} fell in love with a creeper. It was explosive.",
			"\uD83E\uDD26 {player} forgot how gravity works.",
			"\uD83D\uDDA4 {player} died from cringe. Took the world with them.",
			"\uD83E\uDD2A {player} used 100% of their brain... incorrectly.",
			"\uD83E\uDD4A {player} ate a suspicious stew. Then deleted reality.",
			"\uD83C\uDF08 {player} found the rainbow... and destroyed it.",
			"\uD83D\uDD27 {player} tried to fix it. Made it worse.",
			"\uD83D\uDE3F {player} meowed at a Warden. It didn't purr back.",
			"\uD83D\uDC4E {player} was voted off the server. Literally.",
			"\uD83D\uDCBF {player} downloaded RAM. Server crashed.",
			"\uD83C\uDF21 {player} boiled water with redstone. Universe disagreed.",
			"\uD83E\uDD86 {player} tried flying with pigs. Nailed the landing. (Not the survival.)",
			"\uD83D\uDEA8 {player} ignored all warnings. This is the warning.",
			"\uD83C\uDF7E {player} brewed something cursed. It's contagious.",
			"\uD83E\uDDE0 {player} achieved enlightenment. And ascended everyone else too.",
			"\uD83E\uDD43 {player} slipped into the backrooms. Took us all.",
			"\uD83C\uDF0E {player} unzipped the world.zip. It corrupted.",
			"\uD83E\uDE96 {player} stood still for 5 seconds. Fatal error.",
			"\uD83C\uDFA9 {player} wore a cursed hat. Server couldn't handle the drip.",
			"\uD83E\uDD9C {player} poked a bear. It poked back. Globally."
	};

	@Override
	public void onInitialize() {
		System.out.println("KAID Mod Initialized!");

		// Get server reference
		ServerLifecycleEvents.SERVER_STARTED.register(server -> KAID.server = server);

		// Register the command to toggle kill mode
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("kaid_toggle")
					.executes(context -> {
						killAllEnabled = !killAllEnabled;
						String status = killAllEnabled ? "enabled" : "disabled";
						context.getSource().sendFeedback(() -> Text.literal("KAID kill mode is now " + status).formatted(Formatting.GREEN), true);
						return 1;
					})
			);
		});

		// Hook into player death event
		ServerPlayerEvents.ALLOW_DEATH.register((player, source, damage) -> {
			if (server == null || player == null) return true;

			ServerWorld world = (ServerWorld) player.getWorld();
			String playerName = player.getName().getString();
			String message = MESSAGES[RANDOM.nextInt(MESSAGES.length)].replace("{player}", playerName);

			// Send colorful death message with random colors
			Formatting[] colors = Formatting.values();
			Formatting randomColor = colors[RANDOM.nextInt(colors.length)];
			world.getPlayers().forEach(p -> {
				p.sendMessage(Text.literal("⚠ ")
						.formatted(Formatting.YELLOW)
						.append(Text.literal(message)
								.formatted(randomColor, Formatting.BOLD)), false);
			});

			// If kill mode is enabled, kill all players instantly
			if (killAllEnabled) {
				scheduler.schedule(() -> {
					for (ServerPlayerEntity p : world.getPlayers()) {
						p.setHealth(0.0F);
					}
				}, 0, TimeUnit.SECONDS); // No delay
			}

			return true; // Allow death to proceed
		});
	}
}