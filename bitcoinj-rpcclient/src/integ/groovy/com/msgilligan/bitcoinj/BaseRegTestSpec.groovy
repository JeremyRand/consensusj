package com.msgilligan.bitcoinj

import com.msgilligan.bitcoinj.rpc.BitcoinExtendedClient
import com.msgilligan.bitcoinj.rpc.Loggable
import com.msgilligan.bitcoinj.rpc.RPCURI
import com.msgilligan.bitcoinj.test.BTCTestSupport
import com.msgilligan.bitcoinj.test.RegTestFundingSource
import org.bitcoinj.core.Coin
import spock.lang.Specification
import com.msgilligan.bitcoinj.rpc.test.TestServers


/**
 * Abstract Base class for Spock tests of Bitcoin Core in RegTest mode
 */
abstract class BaseRegTestSpec extends Specification implements BTCTestSupport, Loggable {
    static final Coin minBTCForTests = 50.btc
    static final private TestServers testServers = TestServers.instance
    static final protected String rpcTestUser = testServers.rpcTestUser
    static final protected String rpcTestPassword = testServers.rpcTestPassword;

    // Initializer to set up trait properties, Since Spock doesn't allow constructors
    {
        client = new BitcoinExtendedClient(RPCURI.defaultRegTestURI, rpcTestUser, rpcTestPassword)
        fundingSource = new RegTestFundingSource(client)
    }

    void setupSpec() {
        Boolean available = client.waitForServer(60)   // Wait up to 1 minute
        if (!available) {
            log.error "Timeout error."
        }
        assert available

        // Make sure we have enough test coins
        while (getBalance() < minBTCForTests) {
            // Mine blocks until we have some coins to spend
            client.generateBlocks(1)
        }
    }

    /**
     * Clean up after all tests in spec have run.
     */
    void cleanupSpec() {
        // Spend almost all coins as fee, to sweep dust
        consolidateCoins()
    }

}