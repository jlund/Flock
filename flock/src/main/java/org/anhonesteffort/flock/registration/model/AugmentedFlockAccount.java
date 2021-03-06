/*
 * *
 *  Copyright (C) 2014 Open Whisper Systems
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 * /
 */

package org.anhonesteffort.flock.registration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Programmer: rhodey
 */
public class AugmentedFlockAccount extends FlockAccount {

  @JsonProperty
  protected List<FlockSubscription> subscriptions;

  public AugmentedFlockAccount(FlockAccount account, List<FlockSubscription> subscriptions) {
    super(account.getId(),              account.getSalt(),       account.getPasswordSha512(),
         account.getStripeCustomerId(), account.getCreateDate(), account.getLastStripeChargeFailed(),
         account.getAutoRenewEnabled());

    this.subscriptions = subscriptions;
  }

  public AugmentedFlockAccount() {}

  public List<FlockSubscription> getSubscriptions() {
    return subscriptions;
  }

  @JsonIgnore
  public Long getDaysRemaining() {
    long days_expired = 0;

    for (int i = 0; i < subscriptions.size(); i++) {
      if (i == 0)
        days_expired = ((new Date().getTime() - subscriptions.get(i).getCreateDate().getTime())
            / (1000 * 60 * 60 * 24));
      days_expired -= subscriptions.get(i).getDaysCredit();
    }

    return -1 * days_expired;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null)                             return false;
    if (!(other instanceof AugmentedFlockAccount)) return false;

    AugmentedFlockAccount that = (AugmentedFlockAccount)other;
    return super.equals(that) && this.subscriptions.equals(that.subscriptions);
  }

  @Override
  public int hashCode() {
    return super.hashCode() ^ subscriptions.hashCode();
  }

}
