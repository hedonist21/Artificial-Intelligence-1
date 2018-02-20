class DecisionMaking:

    def __init__(self, tranrel={}, reward={}, g=.8):
        self.states = tranrel.keys()
        self.tranrel = tranrel
        self.reward = reward
        self.gamma = g

    def T(self, state, action):
        #Transition model, return a list of (probability, result-state) pairs.
        return self.tranrel[state][action]

    def R(self, state):
        #Returns reward for state.
        return self.reward[state]
    def A(self, state):
        #Actions available for this state.
        return self.tranrel[state].keys()


Transition = {
	'(0, 0)': {
        'U': [(0.8, '(0, 1)'), (0.1, '(1, 0)'), (0.1, '(0, 0)')],
        'D': [(0.8, '(0, 0)'), (0.1, '(0, 0)'), (0.1, '(1, 0)')],
        'R': [(0.8, '(1, 0)'), (0.1, '(0, 0)'), (0.1, '(0, 1)')],
        'L': [(0.8, '(0, 0)'), (0.1, '(0, 1)'), (0.1, '(0, 0)')]
    },
	'(1, 0)': {
        'U': [(0.8, '(1, 0)'), (0.1, '(2, 0)'), (0.1, '(0, 0)')],
        'D': [(0.8, '(1, 0)'), (0.1, '(0, 0)'), (0.1, '(2, 0)')],
        'R': [(0.8, '(2, 0)'), (0.1, '(1, 0)'), (0.1, '(1, 0)')],
        'L': [(0.8, '(0, 0)'), (0.1, '(1, 0)'), (0.1, '(1, 0)')]
    },
	'(2, 0)': {
        'U': [(0.8, '(2, 1)'), (0.1, '(2, 0)'), (0.1, '(1, 0)')],
        'D': [(0.8, '(2, 0)'), (0.1, '(1, 0)'), (0.1, '(2, 0)')],
        'R': [(0.8, '(2, 0)'), (0.1, '(2, 0)'), (0.1, '(2, 1)')],
        'L': [(0.8, '(1, 0)'), (0.1, '(2, 1)'), (0.1, '(2, 0)')]
    },
    '(0, 1)': {
        'U': [(0.8, '(0, 2)'), (0.1, '(0, 1)'), (0.1, '(0, 1)')],
        'D': [(0.8, '(0, 0)'), (0.1, '(0, 1)'), (0.1, '(0, 1)')],
        'R': [(0.8, '(0, 1)'), (0.1, '(0, 0)'), (0.1, '(0, 2)')],
        'L': [(0.8, '(0, 1)'), (0.1, '(0, 2)'), (0.1, '(0, 0)')]
    },
	'(2, 1)': {
        'EXIT': [(0.0, '(2, 1)')]
    },
	'(0, 2)': {
        'U': [(0.8, '(0, 2)'), (0.1, '(1, 2)'), (0.1, '(0, 2)')],
        'D': [(0.8, '(0, 1)'), (0.1, '(0, 2)'), (0.1, '(1, 2)')],
        'R': [(0.8, '(1, 2)'), (0.1, '(0, 1)'), (0.1, '(0, 2)')],
        'L': [(0.8, '(0, 2)'), (0.1, '(0, 2)'), (0.1, '(0, 1)')]
    }, 
    '(1, 2)': {
        'U': [(0.8, '(1, 2)'), (0.1, '(2, 2)'), (0.1, '(0, 2)')],
        'D': [(0.8, '(1, 2)'), (0.1, '(0, 2)'), (0.1, '(2, 2)')],
        'R': [(0.8, '(2, 2)'), (0.1, '(1, 2)'), (0.1, '(1, 2)')],
        'L': [(0.8, '(0, 2)'), (0.1, '(1, 2)'), (0.1, '(1, 2)')]
    },
    '(2, 2)': {
        'EXIT': [(0.0, '(2, 2)')]
    },
}
Rewardvalues = {
    '(0, 0)': -0.04,
    '(1, 0)': -0.04,
    '(2, 0)': -0.04,
    '(0, 1)': -0.04,
    '(1, 1)': None,
    '(2, 1)': -10,
    '(0, 2)': -0.04,
    '(1, 2)': -0.04,
    '(2, 2)': 10
}

markov = DecisionMaking(tranrel=Transition,reward=Rewardvalues)

def opt_policy(U):
    policy = {}
    A = markov.A 
    states = markov.states
    for s in states:
        policy[s] = max(A(s), key=lambda a: expected_util(a, s, U)) #memoization of sub problems.
    return policy
	
def value_iteration():
    epsilon=0.01
    #Solving by value iteration
    A = markov.A 
    states = markov.states
    Rew = markov.R 
    Tran = markov.T
    util = {s: 0 for s in states}
    g = 0.8
    converge = epsilon * (1 - g) / g
    delta = 0
    while True:
        delta = 0
        U = util.copy()
        for s in states:
            util[s] = Rew(s) + g * max([ sum([p * U[s1] for (p, s1) in Tran(s, a)]) for a in A(s)])
            delta = max(delta, abs(util[s] - U[s]))

        if delta < converge:
            return U


def expected_util(a, s, U):
    T = markov.T
    return sum([p * U[s1] for (p, s1) in markov.T(s, a)])



policy = opt_policy(value_iteration())
#printing the optimum policy.
print (policy)
