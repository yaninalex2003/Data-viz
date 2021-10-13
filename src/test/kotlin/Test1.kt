import kotlin.test.*

fun assertEqualsFloat(a:Float, b:Float){
    if (a<b+0.0001 && b<a+0.0001){
        assert(true)
    }
    else assert(false)
}
fun assertListPairFloat (a: List<Pair<Float, Float>>, b: List<Pair<Float, Float>>){
    assertEquals(a.size, b.size)
    for (i in a.indices){
        assertEqualsFloat(a[i].first, b[i].first)
        assertEqualsFloat(a[i].second, b[i].second)
    }
}
internal class Test1 {

    @Test
    fun testMinValueInMap() {
        assertEquals(1F, minValueInMap(mapOf("q" to 10F, "123" to Math.PI.toFloat(), "ans" to 1F)))
    }

    @Test
    fun testAnglesForCircleDiagram() {
        assertContentEquals(listOf(0F, 60F, 180F, 360F), anglesForCircleDiagram(mapOf("q" to 1F, "w" to 2F, "r" to 3F)))
    }

    @Test
    fun testCoordinates() {
        assertListPairFloat(listOf(Pair(60F, 0F),Pair(-120F,0F)), coordinates(mapOf("q" to 1F, "z" to 2F), 0F,0F))
    }

    @Test
    fun testMyFunction() {
        assertListPairFloat(listOf(Pair(0F, 1F)), listOf(Pair(0.000000001F, 1F)))
    }
}
