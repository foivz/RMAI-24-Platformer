package com.hr.foi.rmai_platformer.levels

class TestLevel : LevelData() {
   init {
       tiles.add("p........................")
       tiles.add("ec1......................")
       tiles.add("111........u............t")
       tiles.add("1111111111111111111111111")

       locations.add(Location("LevelCave", 118f, 17f))

       backgroundDataList.add(BackgroundData("forest", -1, -2f, 19f, 4f, 20))
       backgroundDataList.add(BackgroundData("grass", 1, 18f, 22f, 24f, 4))
   }

}