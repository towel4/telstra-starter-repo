Feature: SIM card activation
    Verify that SIM card activation records are correctly saved and reported.

  Scenario: Successful SIM card activation
    Given the SIM card actuator is running
    When I submit an activation request with ICCID "1255789453849037777" and customerEmail "success@example.com"
    Then the activation should be successful for record ID 1

  Scenario: Failed SIM card activation
    Given the SIM card actuator is running
    When I submit an activation request with ICCID "8944500102198304826" and customerEmail "fail@example.com"
    Then the activation should fail for record ID 2
