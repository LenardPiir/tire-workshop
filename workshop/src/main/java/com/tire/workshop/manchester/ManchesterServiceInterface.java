package com.tire.workshop.manchester;

import com.tire.workshop.Domain;

public interface ManchesterServiceInterface {
    Domain getTireChangeTimes(int amount, int page, String from);
}
