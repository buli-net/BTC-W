package com.example.btcwallet

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.wallet.DeterministicSeed
import org.bitcoinj.wallet.Wallet
import java.security.MessageDigest
import java.security.SecureRandom

object WalletManager {
    private const val PREFS_NAME = "btc_wallet_secure"
    private const val KEY_SEED = "seed_phrase"
    private const val KEY_PASSWORD_HASH = "password_hash"

    private fun getEncryptedPrefs(context: Context): EncryptedSharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }

    fun hasWallet(context: Context): Boolean {
        return getEncryptedPrefs(context).contains(KEY_SEED)
    }

    fun verifyPassword(context: Context, password: String): Boolean {
        val prefs = getEncryptedPrefs(context)
        val storedHash = prefs.getString(KEY_PASSWORD_HASH, null)
        return storedHash != null && storedHash == hashPassword(password)
    }

    fun createWallet(context: Context, password: String): List<String> {
        val secureRandom = SecureRandom()
        val entropy = ByteArray(16)
        secureRandom.nextBytes(entropy)
        
        val mnemonic = MnemonicCode.INSTANCE.toMnemonic(entropy)
        val seedPhrase = mnemonic.joinToString(" ")
        
        getEncryptedPrefs(context).edit()
            .putString(KEY_SEED, seedPhrase)
            .putString(KEY_PASSWORD_HASH, hashPassword(password))
            .apply()
            
        return mnemonic
    }

    fun importWallet(context: Context, seedPhrase: String, password: String): Boolean {
        return try {
            val wordList = seedPhrase.trim().split("\\s+".toRegex())
            MnemonicCode.INSTANCE.check(wordList)
            
            getEncryptedPrefs(context).edit()
                .putString(KEY_SEED, seedPhrase.trim())
                .putString(KEY_PASSWORD_HASH, hashPassword(password))
                .apply()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getMnemonic(context: Context, password: String): List<String>? {
        if (!verifyPassword(context, password)) return null
        val prefs = getEncryptedPrefs(context)
        val seedPhrase = prefs.getString(KEY_SEED, null) ?: return null
        return seedPhrase.split(" ")
    }

    fun getAddress(context: Context, password: String): String? {
        val mnemonic = getMnemonic(context, password) ?: return null
        val seed = DeterministicSeed(mnemonic, null, "", System.currentTimeMillis() / 1000)
        val wallet = Wallet.fromSeed(MainNetParams.get(), seed)
        return wallet.currentReceiveAddress().toString()
    }

    fun deleteWallet(context: Context) {
        getEncryptedPrefs(context).edit().clear().apply()
    }
}