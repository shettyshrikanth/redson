

def whileLoop(cond: => Boolean, instruction: Runnable): Unit = {
  if (cond)
    instruction.run();

  whileLoop(cond, instruction);
}