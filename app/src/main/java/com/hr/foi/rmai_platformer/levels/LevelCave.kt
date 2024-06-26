package com.hr.foi.rmai_platformer.levels


class LevelCave: LevelData() {
    init {
        tiles = ArrayList()
        tiles.add("pt................c.c.c.c.......cc.........z.....c.............c.....cccccccc..................4444444444444444..4......")
        tiles.add("e....c..c..c...4444444444...77777777.....444444444444444777774444444cccccccc4444444444444...................m........4..")
        tiles.add("555555555555...............................................s........444444444..........................................c")
        tiles.add("..................d...................................................................d..................444444444444444")
        tiles.add(".......................................................z.............................................7..................")
        tiles.add(".......................g..............c................m..........................................7.....................")
        tiles.add("............c..4.................z....4........................................................7........................")
        tiles.add(".......444f44444444444444477777444444444444444444ff44444444f4444444444f444444...............7...........................")
        tiles.add("....4..666666...............s.................66666666...6666........666............44444...............................")
        tiles.add(".................d.........................r...........e................d...............................................")
        tiles.add("..4.................................................................................................c...................")
        tiles.add("...c...c...c.....4.......................c...........c............................................777777................")
        tiles.add("444444444444444......444.....c...4....44444444.....444444......c..................................s.......4.............")
        tiles.add("...........................4444444............................4444...................44444...................44.........")
        tiles.add(".....................................................................4..........4.......................................")
        tiles.add("..............c......c.................d.......444......................4444.............d........................44....")
        tiles.add(".............44......44.....................4..........................................................r................")
        tiles.add("....m......4444ffffff4444..................4..........................................................................44")
        tiles.add(".........444444ffffff444444....c.c.c..................e.....g................................z...............z..........")
        tiles.add("44444444444444444444444444444444444444444.............44..........4................4444444444444444444777444444444444444")
        tiles.add("44444444444444444444444444444444444444444.............4444444444444..............444444444444444444444444444444444444444")
        tiles.add("44444444444444444444444444444444444444444fffffffffffff444444444444444..c...c...44444444444444444444444444444444444444444")
        tiles.add("444444444444444444444444444444444444444444444444444444444444444444444ff444f444f44444444444444444444444444444444444444444")
        tiles.add("444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444")
        tiles.add("444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444")
        tiles.add("444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444")
        tiles.add("444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444")


        locations.add(Location("LevelCity", 118f, 18f))
        backgroundDataList = ArrayList()
        backgroundDataList.add(BackgroundData("underground", -1, -10f, 25f, 4f, 35))
    }
}



