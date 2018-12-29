package com.n26.transactions.add.infrastructure

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.TextNode
import com.n26.transactions.add.domain.AddTransaction
import java.math.BigDecimal
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.node.IntNode
import com.n26.transactions.statistics.domain.Statistics


class AddTransactionDeserializer : JsonDeserializer<AddTransaction>() {

    @Throws(IllegalArgumentException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): AddTransaction {
        val node: TreeNode?
        try {
            node = jp.codec.readTree(jp)
        } catch (e: Exception) {
            throw IllegalArgumentException("Could not parse this AddTransaction", e)
        }

        try {
            return AddTransaction(readAmount(node), readTimestamp(node))
        } catch (e: Exception) {
            throw IllegalFieldValueException("", e)
        }
    }

    private fun readAmount(node: TreeNode) = BigDecimal(string(node, "amount"))
    private fun readTimestamp(node: TreeNode) = DateUtils.parseDate(string(node, "timestamp"))


    private fun string(node: TreeNode, key: String) = (node.get(key) as TextNode).asText()
}

class StatisticsDeserializer : JsonDeserializer<Statistics>() {

    @Throws(IllegalArgumentException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): Statistics {
        try {
            val node = jp.codec.readTree<TreeNode>(jp)
            return readValues(node)
        } catch (e: Exception) {
            throw IllegalArgumentException("Could not parse this object", e)
        }
    }

    private fun readValues(node: TreeNode): Statistics {
        val result = mutableListOf<Pair<String, String>>()
        var count: Int? = 0
        for (fieldName in node.fieldNames()) {
            if (fieldName == "count") {
                count = (node.get(fieldName) as IntNode).asInt()
            } else {
                result.add(Pair(fieldName, string(node, fieldName)))
            }
        }
        return Statistics.of(count!!, result)
    }

    private fun string(node: TreeNode, key: String) = (node.get(key) as TextNode).asText()
}

class StatisticsSerializer : JsonSerializer<Statistics>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: Statistics, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeStartObject()
        jgen.writeNumberField("count", value.count())
        value.pairs.forEach { (key, value) ->
            jgen.writeStringField(key, value)
        }
        jgen.writeEndObject()
    }
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