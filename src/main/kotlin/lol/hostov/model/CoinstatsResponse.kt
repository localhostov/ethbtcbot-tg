package lol.hostov.model

data class CoinstatsResponse(
    val result: List<Coin>
) {
    data class Coin(
        val symbol: String,
        val price: Float,
        val priceChange1h: Float,
        val priceChange1d: Float,
        val priceChange1w: Float,
    )
}