package io.github.aemogie.timble.utils.console

@Suppress("unused") //maybe can use jline?
enum class ANSIModifier(internal val mod: Int) {
	RESET(0),
	BOLD(1),
	BLACK_FG(30),
	RED_FG(31),
	GREEN_FG(32),
	YELLOW_FG(33),
	BLUE_FG(34),
	MAGENTA_FG(35),
	CYAN_FG(36),
	WHITE_FG(37),
	DEFAULT_FG(39),
	BLACK_BG(40),
	RED_BG(41),
	GREEN_BG(42),
	YELLOW_BG(43),
	BLUE_BG(44),
	MAGENTA_BG(45),
	CYAN_BG(46),
	WHITE_BG(47),
	DEFAULT_BG(49);

	companion object {
		//dk if this adds more overhead, but hey sometimes i wanna feel like im optimizing even when it's the opposite.
		private val cache: HashMap<List<ANSIModifier>, String> = HashMap(32)

		fun of(vararg mod: ANSIModifier): String = cache.computeIfAbsent(mod.toList()) {
			mod.joinToString(
				separator = ";", prefix = "\u001b[", postfix = "m"
			) { it.mod.toString() }
		}
	}
}