# frozen_string_literal: true
class JenkinsJob
  attr_reader :jobName

  def initialize(rootDir, jobName)
    @rootDir = rootDir
    @jobName = jobName
  end

  def exist?
    return Dir.exist?(getJobPath)
  end

  def getJobPath
    return File.join(@rootDir, @jobName)
  end
end
