package com.mafia;

import java.util.ArrayList;
import java.util.List;

public class JailService {
    private static List<Member> jailed = new ArrayList<>();
    public Member setFree(String name) {
        return jailed.stream().filter(m -> m.getName().equals(name)).findFirst().get();
    }

    public void setJailed(Member member) {
        jailed.add(member);
    }
}
