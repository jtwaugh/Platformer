package ogpc.earth2300.resource;

public enum Keywords 
{
	MessageBox 		(1),
	name			(2),
	width			(3),
	height			(4),
	style			(5),
	text			(6),
	Event			(7),
	trigger			(8),
	gameLoad		(9),
	effect			(10),
	Window			(11),
	member			(12),
	display			(13),
	close			(14),
	player			(15),
	lowHP			(16),
	Character		(17),
	sprite			(18),
	weight			(19),
	speed			(20),
	hp				(21),
	pos				(22),
	playerTalk		(23),
	Level			(24),
	currentMap  	(25),
	ChoiceInterface	(26),
	options			(27),
	choiceStyle		(28),
	exitString		(29),
	graphic			(30),
	reward			(31),
	power			(32),
	water			(33),
	Mission			(34),
	Region			(35),
	population		(36),
	industry		(37),
	food 			(38),
	falldamage		(39),
	complete		(40),
	requirements	(41),
	liberate		(42),
	Timer			(43),
	duration		(44),
	start			(45),
	end				(46),
	RandMission		(47),
	foodRiot		(48),
	waterRiot		(49),
	powerRiot		(50),
	greedRiot		(51),
	ladder			(52),
	transform		(53),
	Events			(54),
	ai				(55),
	backdrop		(56),
	proximity		(57),
	x				(58),
	y				(59);
	
	
	 public int code;

	 private Keywords(int c) 
	 {
	   code = c;
	 }
}
