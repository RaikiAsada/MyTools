# frozen_string_literal: true
def calcDifference
  previousNumber = NumberOfFixed.new(10)
  currentNumber = NumberOfFixed.new(15)

  puts currentNumber.calcDifference(previousNumber.number)
end

class NumberOfFixed
  attr_reader :number

  def initialize(number)
    @number = number
  end

  def calcDifference(previousNumber)
    return  @number - previousNumber
  end
end

calcDifference