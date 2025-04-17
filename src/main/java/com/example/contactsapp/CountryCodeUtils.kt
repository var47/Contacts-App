package com.example.contactsapp

object CountryCodeUtils {

    fun getAllCountryCodes(): List<CountryCode> {
        val countries = listOf(
            CountryCode("India", "+91", "\uD83C\uDDEE\uD83C\uDDF3"),
            CountryCode("United States", "+1", "\uD83C\uDDFA\uD83C\uDDF8"),
            CountryCode("United Kingdom", "+44", "\uD83C\uDDEC\uD83C\uDDE7"),
            CountryCode("Canada", "+1", "\uD83C\uDDE8\uD83C\uDDE6"),
            CountryCode("Australia", "+61", "\uD83C\uDDE6\uD83C\uDDFA"),
            CountryCode("Germany", "+49", "\uD83C\uDDE9\uD83C\uDDEA"),
            CountryCode("France", "+33", "\uD83C\uDDEB\uD83C\uDDF7"),
            CountryCode("Japan", "+81", "\uD83C\uDDEF\uD83C\uDDF5"),
            CountryCode("China", "+86", "\uD83C\uDDE8\uD83C\uDDF3"),
            CountryCode("Brazil", "+55", "\uD83C\uDDE7\uD83C\uDDF7")
            // You can keep adding more or fetch from API later
        )
        return countries.sortedBy { it.name }
    }
}
