package com.mmarciniak.diseasetest.data

data class QuestionDataContainer(val trueSymptoms: Array<String>, val falseSymptoms: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuestionDataContainer

        if (!trueSymptoms.contentEquals(other.trueSymptoms)) return false
        if (!falseSymptoms.contentEquals(other.falseSymptoms)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = trueSymptoms.contentHashCode()
        result = 31 * result + falseSymptoms.contentHashCode()
        return result
    }
}