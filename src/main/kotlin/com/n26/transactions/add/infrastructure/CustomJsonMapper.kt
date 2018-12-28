package com.n26.transactions.add.infrastructure

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.n26.transactions.add.infrastructure.delivery.AddTransaction
import java.lang.RuntimeException
import java.math.BigDecimal
import com.fasterxml.jackson.core.JsonProcessingException
import java.io.IOException
import com.fasterxml.jackson.databind.JsonSerializer


class AddTransactionDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<AddTransaction>(vc) {

    @Throws(IllegalArgumentException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): AddTransaction {
//        throw RuntimeException("e")
        try {
            val node = jp.codec.readTree<TreeNode>(jp)
            return AddTransaction(readAmount(node), readTimestamp(node))
        } catch (e: Exception) {
            throw IllegalArgumentException("Could not parse this AddTransaction", e)
        }
    }

    private fun readAmount(node: TreeNode) = BigDecimal(string(node, "amount"))
    private fun readTimestamp(node: TreeNode) = DateUtils.parseDate(string(node, "timestamp"))


    private fun string(node: TreeNode, key: String) = (node.get(key) as TextNode).asText()
}

class AddTransactionSerializer : JsonSerializer<AddTransaction>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: AddTransaction, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeStartObject()
        jgen.writeStringField("amount", value.amount.toString())
        jgen.writeStringField("timestamp", DateUtils.formatted(value.timestamp))
        jgen.writeEndObject()
    }
}